package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.DirectorResponseDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.service.api.DirectorService;
import ru.yandex.practicum.filmorate.service.api.FilmService;
import ru.yandex.practicum.filmorate.service.api.GenreService;
import ru.yandex.practicum.filmorate.service.api.MpaRatingService;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final DirectorService directorService;

    @Override
    public List<FilmResponseDto> findAllFilms() {
        return filmStorage.findAll().stream()
                .map(filmMapper::toDto)
                .toList();
    }

    @Override
    public FilmResponseDto findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId)
                .map(filmMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Film with ID: '%d' is not found".formatted(filmId)));
    }

    @Override
    public LinkedHashSet<FilmResponseDto> showMostPopularFilms(Integer count) {
        return filmStorage.showMostPopularFilms(count)
                .stream()
                .map(filmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
    public List<FilmResponseDto> findCommonFilms(Long userId, Long friendId) {
        return filmStorage.findCommon(userId, friendId).stream()
                .map(filmMapper::toDto)
                .peek(film -> film.setGenres(genreService.getGenresByFilmId(film.getId())))
                .toList();
    }

    @Override
    public List<FilmResponseDto> searchFilms(String query, String by) {

        Set<String> titleByDirector = Arrays.stream(by.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        boolean searchByTitle = titleByDirector.contains("title");
        boolean searchByDirector = titleByDirector.contains("director");

        if (!searchByTitle || !searchByDirector) {
            searchByTitle = true;
            searchByDirector = true;
        }
        log.info("searchFilms(String query, String by): " + filmStorage.searchFilms(query.toLowerCase(), searchByTitle, searchByDirector));
        List<FilmResponseDto> filmResponseDto = filmStorage.searchFilms(query.toLowerCase(), searchByTitle, searchByDirector)
                .stream()
                .peek(film -> film.setGenres(genreService.getGenresByFilmId(film.getId())))
                .map(filmMapper::toDto)
                .peek(film -> film.setDirectors(directorService.findDirectorsByFilmId(film.getId())))
                .toList();
        log.info("Films found filmResponseDto: {}", filmResponseDto);
        return filmResponseDto;
    }

    @Override
    public List<FilmResponseDto> searchFilmsByDirector(Long directorId, String sortBy) {

        if (!"year".equals(sortBy) && !"likes".equals(sortBy)) {
            throw new ValidationException("Invalid sortBy parameter");
        }

        Set<String> sortByYearLikes = Arrays.stream(sortBy.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<DirectorResponseDto> directors = directorService.findDirectorsByDirectorId(directorId);

        boolean sortByYear = sortByYearLikes.contains("year");
        boolean sortByLikes = sortByYearLikes.contains("likes");
        List<FilmResponseDto> filmResponseDto = List.of();
        filmResponseDto = getFilmResponseDtoList(directorId, sortByYear, filmResponseDto, directors, sortByLikes);
        return filmResponseDto;
    }

    private List<FilmResponseDto> getFilmResponseDtoList(Long directorId, boolean sortByYear,
                                                         List<FilmResponseDto> filmResponseDto,
                                                         Set<DirectorResponseDto> directors,
                                                         boolean sortByLikes) {
        if (sortByYear) {
            filmResponseDto = filmStorage.findFilmsByDirector(directorId, "year").stream()
                    .map(filmMapper::toDto)
                    .peek(film -> {
                        film.setDirectors(directors);
                    })
                    .toList();
            log.info("Film search by director: " + filmResponseDto);
        }
        if (sortByLikes) {
            filmResponseDto = filmStorage.findFilmsByDirector(directorId, "likes").stream().map(filmMapper::toDto)
                    .peek(film -> {
                        film.setDirectors(directors);
                    }).toList();
            log.info("Film search by director: " + filmResponseDto);
        }
        return filmResponseDto;
    }

    @Override
    public void removeFilmById(Long filmID) {
        filmStorage.removeFilmById(filmID);
    }
}
