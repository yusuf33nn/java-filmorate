package ru.yandex.practicum.filmorate.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;

import java.util.List;

@RequestMapping(value = "/films")
public interface FilmApi {

    @GetMapping
    ResponseEntity<List<FilmResponseDto>> showAllFilms();

    @GetMapping("/{id}")
    ResponseEntity<FilmResponseDto> findFilmById(@PathVariable Long id);

    @GetMapping("/popular")
    ResponseEntity<List<FilmResponseDto>> showMostPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count);

    @PostMapping
    ResponseEntity<FilmResponseDto> createFilm(@Valid @RequestBody FilmRequestDto film);

    @PutMapping
    ResponseEntity<FilmResponseDto> updateFilm(@Valid @RequestBody FilmRequestDto film);

    @PutMapping("/{id}/like/{userId}")
    ResponseEntity<Void> setLikeToSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);

    @DeleteMapping("/{id}/like/{userId}")
    ResponseEntity<Void> removeLikeFromSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);
}
