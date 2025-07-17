package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.api.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final DirectorStorage directorStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaRatingDbStorage ratingDbStorage;

    @Override
    public List<Film> findAll() {

        return jdbcTemplate.query("select * from film", filmRowMapper)
                .stream()
                .peek(film -> {
                    film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
                    film.setMpa(ratingDbStorage.getMpaRatingById(film.getMpa().getId()).get());
                    film.setLikes(getFilmLikesByFilmId(film.getId()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        Film film = DataAccessUtils.singleResult(
                jdbcTemplate.query("select * from film where id = ?", filmRowMapper, filmId)
        );
        if (film != null) {
            film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
            film.setMpa(ratingDbStorage.getMpaRatingById(film.getMpa().getId()).get());
            film.setLikes(getFilmLikesByFilmId(film.getId()));
        }
        return Optional.ofNullable(film);
    }

    @Override
    public LinkedHashSet<Film> showMostPopularFilms(Integer count, Integer genreId, Integer year) {
        String sql = """
                   SELECT  f.*, (SELECT
                                   COUNT(fL. user_id) AS like_count
                               FROM film_like as fl
                               WHERE FL.FILM_ID = f.id
                               GROUP BY film_id
                               ORDER BY like_count DESC
                               LIMIT ?) AS like_count
                   FROM    film f
                   WHERE
                      ( ? IS NULL
                            OR EXTRACT(YEAR FROM f.release_date) = ? )
                     
                      AND ( ? IS NULL
                            OR EXISTS ( SELECT 1
                                        FROM   film_genre fg
                                        WHERE  fg.film_id  = f.id
                                          AND  fg.genre_id = ? ) )
                    ORDER BY like_count DESC, f.name;
                """;

        return (jdbcTemplate.query(sql, filmRowMapper, count, year, year, genreId, genreId)).stream()
                .peek(film -> {
                    film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
                    film.setMpa(ratingDbStorage.getMpaRatingById(film.getMpa().getId()).orElse(null));
                    film.setLikes(getFilmLikesByFilmId(film.getId()));
                }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Film saveFilm(Film film) {
        List<Long> listDirectors = film.getDirectors().stream()
                .map(Director::getId)
                .filter(id -> id != 0)
                .toList();
        Collection<Director> directorCollection = directorStorage.findDirectorsByParams(listDirectors);

        if (listDirectors.size() != directorCollection.size()) {
            throw new RuntimeException("Directors not found");
        }
        film.setDirectors(new HashSet<>(directorCollection));

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
        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
        film.setMpa(ratingDbStorage.getMpaRatingById(film.getMpa().getId()).get());
        film.setLikes(getFilmLikesByFilmId(film.getId()));
        film.setId(generatedId);

        if (!film.getDirectors().isEmpty()) {
            String filmDirectorsSql = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
            List<Object[]> batchArgs = film.getDirectors().stream()
                    .map(director -> new Object[]{film.getId(), director.getId()})
                    .collect(Collectors.toList());

            jdbcTemplate.batchUpdate(filmDirectorsSql, batchArgs);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        List<Long> listDirectors = film.getDirectors().stream()
                .map(Director::getId)
                .filter(id -> id != 0)
                .toList();
        Collection<Director> directorCollection = directorStorage.findDirectorsByParams(listDirectors);

        if (listDirectors.size() != directorCollection.size()) {
            throw new RuntimeException("Directors not found");
        }

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
        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
        film.setMpa(ratingDbStorage.getMpaRatingById(film.getMpa().getId()).get());
        film.setLikes(getFilmLikesByFilmId(film.getId()));

        String deleteFilmDirector = "DELETE FROM film_director WHERE film_id = ?";
        jdbcTemplate.update(deleteFilmDirector, film.getId());

        film.setDirectors(new HashSet<>(directorCollection));

        if (!film.getDirectors().isEmpty()) {
            String filmDirectorsSql = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
            List<Object[]> batchArgs = film.getDirectors().stream()
                    .map(director -> new Object[]{film.getId(), director.getId()})
                    .collect(Collectors.toList());

            jdbcTemplate.batchUpdate(filmDirectorsSql, batchArgs);
        }
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
    public List<Film> searchFilms(String query, Boolean searchByTitle, Boolean searchByDirector) {
        String sql;

        if (searchByTitle && searchByDirector) {
            return getFilmsBySearchDirector(query);
        }
        if (searchByTitle) {
            return getFilmsByTitle(query);
        }
        if (searchByDirector) {
            return getFilmsByDirector(query);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Film> findFilmsByDirector(Long directorId, String sortBy) {
        System.out.println("directorId" + directorId);
        System.out.println("sortBy" + sortBy);
        String sql;
        if ("year".equals(sortBy)) {
            return getFilmsSortByYear(directorId);
        } else
        if ("likes".equals(sortBy)) {
            return getFilmsSortByLikes(directorId);
        } else {
            throw new IllegalArgumentException("Invalid sortBy parameter");
        }
    }

    @Override
    public List<Film> findCommon(Long userId, Long friendId) {
        return getFilmsUserFriend(userId, friendId);
    }

    private List<Film> getFilmsUserFriend(Long userId, Long friendId) {
        String sql = """
                SELECT * FROM FILM f WHERE
                    id IN (
                            SELECT l.FILM_ID FROM FILM_LIKE l WHERE l.USER_ID = ?
                            INTERSECT
                            SELECT l.FILM_ID  FROM FILM_LIKE l  WHERE l.USER_ID = ?
                )
               """;
        return jdbcTemplate.query(sql, filmRowMapper, userId, friendId);
    }

    @Transactional
    @Override
    public void removeFilmById(Long filmId) {
        if (!filmExists(filmId)) {
            throw new NotFoundException("Фильм с ID " + filmId + " не найден");
        }

        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        jdbcTemplate.update("DELETE FROM film_like WHERE film_id = ?", filmId);
        jdbcTemplate.update("DELETE FROM film WHERE id = ?", filmId);
    }

    private boolean filmExists(Long filmId) {
        String sql = "SELECT COUNT(*) FROM film WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, filmId) > 0;
    }



    private List<Film> getFilmsSortByLikes(Long directorId) {
        String sql;
        sql = """
                SELECT f.*, COUNT(fl.user_id) as like_count
                FROM film f
                JOIN film_director fd ON f.id = fd.film_id
                LEFT JOIN film_like fl ON f.id = fl.film_id
                WHERE fd.director_id = ?
                GROUP BY f.id
                ORDER BY like_count desc
                """;
        return jdbcTemplate.query(sql, filmRowMapper, directorId);
    }

    private List<Film> getFilmsSortByYear(Long directorId) {
        String sql;
        sql = """
                SELECT f.*
                FROM film f
                JOIN film_director fd ON f.id = fd.film_id
                WHERE fd.director_id = ?
                ORDER BY f.release_date
                """;
        return jdbcTemplate.query(sql, filmRowMapper, directorId);
    }

    private List<Film> getFilmsByDirector(String query) {
        String sql;
        sql = """
                SELECT f.*
                FROM FILM f
                LEFT JOIN FILM_DIRECTOR fd ON f.id = fd.film_id
                LEFT JOIN DIRECTORS d ON fd.DIRECTOR_id = d.id
                WHERE d.name ILIKE ?
                ORDER BY (SELECT COUNT(*) FROM FILM_LIKE WHERE FILM_ID = f.ID)
                """;
        return jdbcTemplate.query(sql, new Object[]{"%" + query + "%"}, filmRowMapper);
    }

    private List<Film> getFilmsByTitle(String query) {
        String sql;
        sql = """
                SELECT *
                FROM FILM f
                WHERE f.name ILIKE ?
                ORDER BY (SELECT COUNT(*) FROM FILM_LIKE WHERE FILM_ID = f.ID)
                """;
        return jdbcTemplate.query(sql, new Object[]{"%" + query + "%"}, filmRowMapper);
    }

    private List<Film> getFilmsBySearchDirector(String query) {
        String sql;
        sql = """
                SELECT f.*
                FROM FILM f
                    LEFT JOIN FILM_DIRECTOR fd ON f.id = fd.film_id
                    LEFT JOIN DIRECTORS d ON fd.DIRECTOR_id = d.id
                WHERE f.NAME ILIKE ? OR d.NAME ILIKE ?
                ORDER BY
                    (SELECT COUNT(*) FROM FILM_LIKE WHERE FILM_ID = f.ID)
                """;
        return jdbcTemplate.query(sql, new Object[]{"%" + query + "%", "%" + query + "%"}, filmRowMapper);
    }
}
