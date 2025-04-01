package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private final AtomicLong filmId = new AtomicLong(0L);

    private final Map<Long, Film> movies = new HashMap<>();

    @PostMapping
    public ResponseEntity<Film> createMovie(@Valid @RequestBody Film film) {
        log.info("Request Body: {}", film);
        Long filmId = this.filmId.incrementAndGet();
        film.setId(filmId);
        movies.put(filmId, film);
        return ResponseEntity.status(CREATED).body(film);
    }

    @GetMapping
    public ResponseEntity<List<Film>> showAllMovies() {
        return ResponseEntity.ok(movies.values().stream().toList());
    }

    @PutMapping
    public ResponseEntity<Film> updateMovie(@Valid @RequestBody Film film) {
        Long filmId = film.getId();
        if (filmId == null || filmId == 0) {
            log.error("Film id cannot be null or zero for update operation");
            return ResponseEntity.badRequest().build();
        }
        if (movies.get(filmId) == null) {
            log.error("You cannot update film, if film doesn't exist");
            return ResponseEntity.internalServerError().body(film);
        }
        movies.put(filmId, film);
        return ResponseEntity.ok(film);
    }
}
