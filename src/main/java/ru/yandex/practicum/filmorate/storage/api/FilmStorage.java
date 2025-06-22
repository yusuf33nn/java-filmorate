package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    List<Film> findAll();

    Optional<Film> findFilmById(Long filmId);

    Set<Film> showMostPopularFilms(Integer count);

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    void setLikeToSpecificFilmByUser(Long filmId, Long userId);

    void removeLikeFromSpecificFilmByUser(Long filmId, Long userId);
}
