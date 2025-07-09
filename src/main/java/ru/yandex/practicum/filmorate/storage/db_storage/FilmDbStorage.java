package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query("select * from film", filmRowMapper);
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from film where id = ?", filmRowMapper, filmId)
                )
        );
    }

    @Override
    public Set<Film> showMostPopularFilms(Integer count) {
        String sql = """
                   SELECT  f.*, l.like_count
                     FROM    film f
                     JOIN
                            (SELECT film_id,
                                    COUNT(fl.user_id) AS like_count
                               FROM film_like as fl
                           GROUP BY film_id
                           ORDER BY like_count DESC
                              LIMIT ?) l
                       ON l.film_id = f.id
                ORDER  BY l.like_count DESC, f.name;
                """;

        return new LinkedHashSet<>(jdbcTemplate.query(sql, filmRowMapper, count));
    }

    @Override
    public Film saveFilm(Film film) {
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, DURATION, RELEASE_DATE, MPA_RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());          // «login» здесь — обычная колонка
            ps.setLong(3, film.getDuration());
            ps.setObject(4, film.getReleaseDate());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, kh);
        var generatedId = Optional.ofNullable(kh.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new RuntimeException("Id is not created"));
        film.setId(generatedId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = """
                UPDATE FILM
                   SET name          = ?,
                       description   = ?,
                       duration      = ?,
                       release_date  = ?,
                       mpa_rating_id = ?
                 WHERE id            = ?
                """;

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public void setLikeToSpecificFilmByUser(Long filmId, Long userId) {
        String sql = "INSERT INTO FILM_LIKE (FILM_ID, USER_ID)  VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLikeFromSpecificFilmByUser(Long filmId, Long userId) {
        String sql = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Set<Long> getFilmLikesByFilmId(Long filmId) {
        String sql = "SELECT USER_ID FROM FILM_LIKE WHERE FILM_ID = ?";
        return Set.copyOf(jdbcTemplate.queryForList(sql, Long.class, filmId));
    }

    @Override
    public List<Film> searchFilms(String query, Boolean searchByTitle) {
        List<Film> films;
        if (searchByTitle){
            String sql = """
            SELECT * 
            FROM FILM f 
            WHERE f.name ilike ? 
            ORDER BY (SELECT COUNT(*) FROM FILM_LIKE WHERE FILM_ID = f.ID) desc
            """;
            films = jdbcTemplate.query(sql,new Object[]{"%" + query + "%"},filmRowMapper);
        } else {
            films = Collections.emptyList();
        }
        return films;
    }
}
