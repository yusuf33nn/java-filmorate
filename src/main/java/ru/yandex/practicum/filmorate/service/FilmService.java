package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> findAllFilms();

    Film findFilmById(Long filmId);

    List<Film> showMostPopularFilms(Integer count);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film setLikeToSpecificFilmByUser(Long filmId, Long userId);

    Film removeLikeFromSpecificFilmByUser(Long filmId, Long userId);
}
