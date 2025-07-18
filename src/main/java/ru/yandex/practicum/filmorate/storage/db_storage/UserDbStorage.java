package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.row.UserRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.api.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final GenreDbStorage genreDbStorage;
    private final MpaRatingDbStorage ratingDbStorage;

    @Override
    public List<User> showAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from users where id = ?", userRowMapper, userId)
                )
        );
    }

    @Override
    public User saveUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());          // «login» здесь — обычная колонка
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, kh);
        var generatedId = Optional.ofNullable(kh.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new RuntimeException("Id is not created"));
        user.setId(generatedId);
        return user;
    }

    @Override
    public int updateUser(User user) {
        var sql = """
                UPDATE users
                   SET email    = ?,
                       login    = ?,
                       name     = ?,
                       birthday = ?
                 WHERE id       = ?
                """;
        return jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public void deleteUser(Long userId) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
    }

    @Override
    public List<User> findSimilarUsers(Long userId) {
        String sql = """
                SELECT
                u.id,
                u.email,
                u.login,
                u.name,
                u.birthday,
                COUNT(*) as likes_count
                FROM film_like fl1
                JOIN film_like fl2 ON fl1.film_id = fl2.film_id
                JOIN users u ON fl2.user_id = u.id
                WHERE fl1.user_id = ?
                AND fl2.user_id != ?
                GROUP BY u.id
                ORDER BY likes_count DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        User.builder()
                                .id(rs.getLong("id"))
                                .email(rs.getString("email"))
                                .login(rs.getString("login"))
                                .name(rs.getString("name"))
                                .birthday(rs.getDate("birthday").toLocalDate())
                                .likesCount(rs.getInt("likes_count"))
                                .build(),
                userId, userId);
    }

    @Override
    public List<Film> getTopRecommendations(Long userId) {
        Map<Long, Integer> filmRating = findSimilarUsers(userId)
                .stream()
                .limit(10)
                .flatMap(user -> findFilmsLikedByUser(user.getId()).stream())
                .filter(film -> !hasLikedFilm(userId, film.getId()))
                .collect(Collectors.toMap(
                        Film::getId,
                        film -> 1,
                        Integer::sum
                ));

        return filmRating.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> findFilmById(entry.getKey()))
                .peek(film -> {
                        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
                        film.setMpa(ratingDbStorage.getMpaRatingById(film.getMpa().getId()).orElse(null));})
                .collect(Collectors.toList());
    }


    private List<Film> findFilmsLikedByUser(Long userId) {
        String sql = """
                SELECT f.*
                FROM film_like fl
                JOIN film f ON fl.film_id = f.id
                WHERE fl.user_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        Film.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .description(rs.getString("description"))
                                .releaseDate(rs.getDate("release_date").toLocalDate())
                                .duration(rs.getLong("duration"))
                                .mpa(MpaRating.builder()
                                        .id(rs.getInt("mpa_rating_id"))
                                        .build())
                                .build(),
                userId);
    }

    private Film findFilmById(Long filmId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM film WHERE id = ?",
                (rs, rowNum) ->
                        Film.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .description(rs.getString("description"))
                                .releaseDate(rs.getDate("release_date").toLocalDate())
                                .duration(rs.getLong("duration"))
                                .mpa(MpaRating.builder()
                                        .id(rs.getInt("mpa_rating_id"))
                                        .build())
                                .build(),
                filmId);
    }

    private boolean hasLikedFilm(Long userId, Long filmId) {
        String sql = "SELECT COUNT(*) FROM film_like WHERE user_id = ? AND film_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, filmId) > 0;
    }


    @Transactional
    @Override
    public void removeUserById(Long userId) {
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE id = ?";
        int userExists = jdbcTemplate.queryForObject(checkUserSql, Integer.class, userId);

        if (userExists == 0) {
            throw new NotFoundException("Пользователь не найден");
        }

        try {
            /*jdbcTemplate.update("DELETE FROM REVIEWS_GRADES WHERE userId = ?", userId);
            jdbcTemplate.update("DELETE FROM REVIEWS_GRADES WHERE review_id =" +
                    " (select id from reviews where user_id = ?)", userId);*/
            jdbcTemplate.update("DELETE FROM REVIEWS WHERE user_id = ?", userId);
            jdbcTemplate.update("DELETE FROM friendship WHERE requester_id = ?", userId);
            jdbcTemplate.update("DELETE FROM friendship WHERE receiver_id = ?", userId);
            jdbcTemplate.update("DELETE FROM film_like WHERE user_id = ?", userId);
            jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }


}
