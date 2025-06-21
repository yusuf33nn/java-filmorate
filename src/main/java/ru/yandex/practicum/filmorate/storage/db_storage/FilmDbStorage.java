package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.queryForList("select * from film", Film.class);
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("select * from film f where f.id = :filmId", Film.class));
    }

    @Override
    public List<Film> showMostPopularFilms(Integer count) {
        return List.of();
    }

    @Override
    public void saveFilm(Film film) {

    }
}
