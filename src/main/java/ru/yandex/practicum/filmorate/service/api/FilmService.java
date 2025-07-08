package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;

import java.util.List;

public interface FilmService {

    List<FilmResponseDto> findAllFilms();

    FilmResponseDto findFilmById(Long filmId);

    List<FilmResponseDto> showMostPopularFilms(Integer count);

    FilmResponseDto createFilm(FilmRequestDto film);

    FilmResponseDto updateFilm(FilmRequestDto film);

    void setLikeToSpecificFilmByUser(Long filmId, Long userId);

    void removeLikeFromSpecificFilmByUser(Long filmId, Long userId);

    List<FilmResponseDto> searchFilms(String query, String by);
}
