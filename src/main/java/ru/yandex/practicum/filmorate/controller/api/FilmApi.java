package ru.yandex.practicum.filmorate.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    ResponseEntity<List<FilmResponseDto>> showMostPopularFilms(@RequestParam(name = "count", defaultValue = "10")
                                                               @Valid @Max(10000)
                                                               @Positive(message = "Count должен быть больше 0")
                                                               int count);

    @PostMapping
    ResponseEntity<FilmResponseDto> createFilm(@Valid @RequestBody FilmRequestDto film);

    @PutMapping
    ResponseEntity<FilmResponseDto> updateFilm(@Valid @RequestBody FilmRequestDto film);

    @PutMapping("/{id}/like/{userId}")
    ResponseEntity<Void> setLikeToSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);

    @DeleteMapping("/{id}/like/{userId}")
    ResponseEntity<Void> removeLikeFromSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);
}
