package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.FilmApi;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController implements FilmApi {

    private final FilmService filmService;


    public ResponseEntity<List<Film>> showAllFilms() {
        return ResponseEntity.ok(filmService.findAllFilms());
    }

    @Override
    public ResponseEntity<Film> findFilmById(Long id) {
        return ResponseEntity.ok(filmService.findFilmById(id));
    }

    @Override
    public ResponseEntity<List<Film>> showMostPopularFilms(int count) {
        return ResponseEntity.ok(filmService.showMostPopularFilms(count));
    }

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        log.info("Request Body: {}", film);

        return ResponseEntity.status(CREATED).body(filmService.createFilm(film));
    }

    @Override
    public ResponseEntity<Film> updateFilm(Film film) {
        log.info("Request Body: {}", film);
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    @Override
    public ResponseEntity<Film> setLikeToSpecificFilmByUser(Long id, Long userId) {
        return ResponseEntity.ok(filmService.setLikeToSpecificFilmByUser(id, userId));
    }

    @Override
    public ResponseEntity<Void> removeLikeFromSpecificFilmByUser(Long id, Long userId) {
        filmService.removeLikeFromSpecificFilmByUser(id, userId);
        return ResponseEntity.ok().build();
    }
}
