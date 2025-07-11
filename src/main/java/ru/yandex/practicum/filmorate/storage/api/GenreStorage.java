package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(Integer id);

    void addGenreToFilm(Integer genreId, Long filmId);

    Set<Genre> getGenresByFilmId(Long filmId);
}
