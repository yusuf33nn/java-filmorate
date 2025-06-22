package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(Integer id);

    void addGenreToFilm(Integer genreId, Long filmId);
}
