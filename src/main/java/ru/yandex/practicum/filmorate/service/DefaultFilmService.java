package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.service.api.FilmService;
import ru.yandex.practicum.filmorate.service.api.GenreService;
import ru.yandex.practicum.filmorate.service.api.MpaRatingService;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultFilmService implements FilmService {

    @Qualifier(value = "filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final FilmMapper filmMapper;
    private final GenreService genreService;
    private final MpaRatingService mpaRatingService;

    @Override
    public List<FilmResponseDto> findAllFilms() {
        return filmStorage.findAll().stream()
                .map(filmMapper::toDto)
                .toList();
    }

    @Override
    public FilmResponseDto findFilmById(Long filmId) {
        FilmResponseDto responseDto = filmStorage.findFilmById(filmId)
                .map(filmMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Film with ID: '%d' is not found".formatted(filmId)));
        MpaDto mpa = mpaRatingService.getMpaRatingById(responseDto.getMpa().getId());
        responseDto.setMpa(mpa);
        responseDto.setGenres(genreService.getGenresByFilmId(filmId));
        responseDto.setLikes(filmStorage.getFilmLikesByFilmId(filmId));
        return responseDto;
    }

    @Override
    public List<FilmResponseDto> showMostPopularFilms(Integer count) {
        return filmStorage.showMostPopularFilms(count).stream().map(filmMapper::toDto).toList();
    }

    @Override
    public FilmResponseDto createFilm(FilmRequestDto filmDto) {
        Film filmEntity = filmMapper.toEntity(filmDto);
        mpaRatingService.getMpaRatingById(filmDto.getMpa().getId());
        filmEntity = filmStorage.saveFilm(filmEntity);
        final var savedFilmId = filmEntity.getId();
        filmDto.getGenres()
                .forEach(genreEntity -> {
                    genreService.getGenreById(genreEntity.getId());
                    findFilmById(savedFilmId);
                    genreService.addGenreToFilm(genreEntity.getId(), savedFilmId);
                });
        filmEntity.setGenres(filmDto.getGenres());
        return filmMapper.toDto(filmEntity);
    }

    @Override
    public FilmResponseDto updateFilm(FilmRequestDto filmDto) {
        Long filmId = filmDto.getId();
        if (filmId == null || filmId == 0) {
            log.error("Film id cannot be null or zero for update operation");
            throw new RuntimeException();
        }
        findFilmById(filmId);
        Film film = filmStorage.updateFilm(filmMapper.toEntity(filmDto));
        return filmMapper.toDto(film);
    }

    @Override
    public void setLikeToSpecificFilmByUser(Long filmId, Long userId) {
        findFilmById(filmId);
        userService.findUserById(userId);
        filmStorage.setLikeToSpecificFilmByUser(filmId, userId);
    }

    @Override
    public void removeLikeFromSpecificFilmByUser(Long filmId, Long userId) {
        findFilmById(filmId);
        userService.findUserById(userId);
        filmStorage.removeLikeFromSpecificFilmByUser(filmId, userId);
    }

    @Override
    public List<FilmResponseDto> searchFilms(String query, String by) {
        String[] searchBy = by.split(",");

        boolean searchByTitle = false;

        for (String s : searchBy) {
            if (s.equalsIgnoreCase("title")) {
                searchByTitle = true;
            }
        }

        if (!searchByTitle) {
            searchByTitle = true;
        }
        return filmStorage.searchFilms(query.toLowerCase(),searchByTitle).stream().map(filmMapper::toDto).toList();
    }
}
