package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Optional<Film> findFilmById(Long filmId);

    List<Film> showMostPopularFilms(Integer count);

    void saveFilm(Film film);
}
