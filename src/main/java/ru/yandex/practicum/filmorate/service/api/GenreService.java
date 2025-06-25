package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    void addGenreToFilm(Integer genreId, Long filmId);

    Set<Genre> getGenresByFilmId(Long filmId);
}
