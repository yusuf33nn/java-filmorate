package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Optional<Film> findFilmById(Long filmId);

    List<Film> showMostPopularFilms(Integer count);

    Film saveFilm(Film film);

    void setLikeToSpecificFilmByUser(Long filmId, Long userId);

    void removeLikeFromSpecificFilmByUser(Long filmId, Long userId);
}
