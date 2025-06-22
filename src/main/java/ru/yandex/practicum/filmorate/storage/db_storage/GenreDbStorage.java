package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.storage.api.GenreStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genre", genreRowMapper);
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from genre where id = ?", genreRowMapper, id)
                )
        );
    }

    @Override
    public void addGenreToFilm(Integer genreId, Long filmId) {
        jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)", filmId, genreId);
    }
}
