package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.dto.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.dto.GenreMapper;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.DirectorResponseDto;
import ru.yandex.practicum.filmorate.model.dto.response.EventType;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.dto.response.Operation;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;
import ru.yandex.practicum.filmorate.service.api.DirectorService;
import ru.yandex.practicum.filmorate.service.api.FilmService;
import ru.yandex.practicum.filmorate.service.api.GenreService;
import ru.yandex.practicum.filmorate.service.api.MpaRatingService;
import ru.yandex.practicum.filmorate.service.api.UserEventFeedService;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.time.LocalDate;
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
    private final GenreService genreService;
    private final MpaRatingService mpaRatingService;
    private final DirectorService directorService;
    private final UserEventFeedService userEventFeedService;

    @Override
    public List<FilmResponseDto> findAllFilms() {
        return filmStorage.findAll().stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    @Override
    public FilmResponseDto findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId)
                .map(FilmMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Film with ID: '%d' is not found".formatted(filmId)));
    }

    @Override
    public LinkedHashSet<FilmResponseDto> showMostPopularFilms(Integer count, Integer genreId, Integer year) {
        return filmStorage.showMostPopularFilms(count, genreId, year)
                .stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public FilmResponseDto createFilm(FilmRequestDto filmDto) {
        Film filmEntity = FilmMapper.toEntity(filmDto);
        mpaRatingService.getMpaRatingById(filmDto.getMpa().getId());
        filmEntity = filmStorage.saveFilm(filmEntity);
        return FilmMapper.toDto(filmEntity);
    }

    @Override
    public FilmResponseDto updateFilm(FilmRequestDto filmDto) {
        Long filmId = filmDto.getId();
        if (filmId == null || filmId == 0) {
            throw new RuntimeException("Film id cannot be null or zero for update operation");
        }
        findFilmById(filmId);
        Film film = filmStorage.updateFilm(FilmMapper.toEntity(filmDto));
        return FilmMapper.toDto(film);
    }

    @Override
    public void setLikeToSpecificFilmByUser(Long filmId, Long userId) {
        findFilmById(filmId);
        userService.findUserById(userId);
        filmStorage.setLikeToSpecificFilmByUser(filmId, userId);
        userEventFeedService.saveEvent(createLikeEvent(userId, filmId, Operation.ADD));
    }

    @Override
    public void removeLikeFromSpecificFilmByUser(Long filmId, Long userId) {
        findFilmById(filmId);
        userService.findUserById(userId);
        filmStorage.removeLikeFromSpecificFilmByUser(filmId, userId);
        userEventFeedService.saveEvent(createLikeEvent(userId, filmId, Operation.REMOVE));
    }

    @Override
    public List<FilmResponseDto> findCommonFilms(Long userId, Long friendId) {
        return filmStorage.findCommon(userId, friendId).stream()
                .map(FilmMapper::toDto)
                .peek(film -> {
                    var genreDtoSet = genreService.getGenresByFilmId(film.getId()).stream()
                            .map(GenreMapper::toDto)
                            .collect(Collectors.toSet());
                    film.setGenres(genreDtoSet);
                    film.setMpa(mpaRatingService.getMpaRatingById(film.getMpa().getId()));
                })
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

        log.info("searchFilms(String query, String by): " + filmStorage.searchFilms(query.toLowerCase(), searchByTitle, searchByDirector));
        List<FilmResponseDto> filmResponseDto = filmStorage.searchFilms(query.toLowerCase(), searchByTitle, searchByDirector)
                .stream()
                .peek(film -> film.setGenres(genreService.getGenresByFilmId(film.getId())))
                .map(FilmMapper::toDto)
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

        directorService.findDirectorById(directorId);

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
                    .map(FilmMapper::toDto)
                    .peek(film -> {
                        film.setDirectors(directors);
                    })
                    .toList();
            log.info("Film search by director: " + filmResponseDto);
        }
        if (sortByLikes) {
            filmResponseDto = filmStorage.findFilmsByDirector(directorId, "likes").stream()
                    .map(FilmMapper::toDto)
                    .peek(film -> film.setDirectors(directors))
                    .toList();
            log.info("Film search by director: {}", filmResponseDto);
        }
        return filmResponseDto;
    }

    @Override
    public void removeFilmById(Long filmID) {
        filmStorage.removeFilmById(filmID);
    }

    private UserEventFeed createLikeEvent(Long userId, Long filmId, Operation operation) {
        return UserEventFeed.builder()
                .userId(userId)
                .entityId(filmId)
                .eventType(EventType.LIKE)
                .operation(operation)
                .timestamp(LocalDate.now())
                .build();
    }
}
