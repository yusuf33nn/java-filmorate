package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.FilmApi;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.service.api.FilmService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController implements FilmApi {

    private final FilmService filmService;


    public ResponseEntity<List<FilmResponseDto>> showAllFilms() {
        return ResponseEntity.ok(filmService.findAllFilms());
    }

    @Override
    public ResponseEntity<FilmResponseDto> findFilmById(Long id) {
        log.info("Find film by id: {}", id);
        return ResponseEntity.ok(filmService.findFilmById(id));
    }

    @Override
    public ResponseEntity<List<FilmResponseDto>> showMostPopularFilms(int count) {
        return ResponseEntity.ok(filmService.showMostPopularFilms(count));
    }

    @Override
    public ResponseEntity<List<FilmResponseDto>> searchFilms(String query, String by) {
        log.info("searchFilms: {} {}", query, by);
        return ResponseEntity.ok(filmService.searchFilms(query, by));
    }

    @Override
    public ResponseEntity<List<FilmResponseDto>> searchFilmsByDirector(Long directorId, String sortBy) {
        log.info("Getting films by director ID: {}, sorted by: {}", directorId, sortBy);

        if (!"year".equals(sortBy) && !"likes".equals(sortBy)) {
            throw new ValidationException("Invalid sortBy parameter");
        }

        return ResponseEntity.ok(filmService.searchFilmsByDirector(directorId, sortBy));
    }

    @Override
    public ResponseEntity<FilmResponseDto> createFilm(FilmRequestDto film) {
        log.info("Request Film create: {}", film);
        return ResponseEntity.status(CREATED).body(filmService.createFilm(film));
    }

    @Override
    public ResponseEntity<FilmResponseDto> updateFilm(FilmRequestDto film) {
        log.info("Request Update Body: {}", film);
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    @Override
    public ResponseEntity<Void> setLikeToSpecificFilmByUser(Long id, Long userId) {
        filmService.setLikeToSpecificFilmByUser(id, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeLikeFromSpecificFilmByUser(Long id, Long userId) {
        filmService.removeLikeFromSpecificFilmByUser(id, userId);
        return ResponseEntity.ok().build();
    }
}
