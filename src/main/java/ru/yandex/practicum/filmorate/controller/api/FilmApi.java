package ru.yandex.practicum.filmorate.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RequestMapping(value = "/films")
public interface FilmApi {

    @GetMapping
    ResponseEntity<List<Film>> showAllFilms();

    @GetMapping("/{id}")
    ResponseEntity<Film> findFilmById(@PathVariable Long id);

    @GetMapping("/popular?count={count}")
    ResponseEntity<List<Film>> showMostPopularFilms(@RequestParam(defaultValue = "10") Integer count);

    @PostMapping
    ResponseEntity<Film> createFilm(@Valid @RequestBody Film film);

    @PutMapping
    ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film);

    @PutMapping("/{id}/like/{userId}")
    ResponseEntity<Film> setLikeToSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);

    @DeleteMapping("/{id}/like/{userId}")
    ResponseEntity<Void> removeLikeFromSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);
}
