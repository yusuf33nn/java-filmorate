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
import java.util.List;
import java.util.Optional;

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
    public List<Film> showMostPopularFilms(Integer count) {
        return List.of();
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
    public void setLikeToSpecificFilmByUser(Long filmId,  Long userId) {
        String sql = "INSERT INTO FILM_LIKE (FILM_ID, USER_ID)  VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLikeFromSpecificFilmByUser(Long filmId, Long userId) {
        String sql = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
