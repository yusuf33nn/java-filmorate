package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;

import java.util.List;
import java.util.Set;

public interface FilmService {

    List<FilmResponseDto> findAllFilms();

    FilmResponseDto findFilmById(Long filmId);

    Set<FilmResponseDto> showMostPopularFilms(Integer count, Integer genreId, Integer year);

    FilmResponseDto createFilm(FilmRequestDto film);

    FilmResponseDto updateFilm(FilmRequestDto film);

    void setLikeToSpecificFilmByUser(Long filmId, Long userId);

    void removeLikeFromSpecificFilmByUser(Long filmId, Long userId);

    List<FilmResponseDto> findCommonFilms(Long userId, Long friendId);

    List<FilmResponseDto> searchFilms(String query, String by);

    List<FilmResponseDto> searchFilmsByDirector(Long id, String by);

    void removeFilmById(Long filmID);
}
