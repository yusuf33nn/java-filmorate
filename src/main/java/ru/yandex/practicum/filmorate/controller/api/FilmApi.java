package ru.yandex.practicum.filmorate.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import java.util.Set;

@RequestMapping(value = "/films")
public interface FilmApi {

    @GetMapping
    ResponseEntity<List<FilmResponseDto>> showAllFilms();

    @GetMapping("/{id}")
    ResponseEntity<FilmResponseDto> findFilmById(@PathVariable Long id);

    @GetMapping("/popular")
    ResponseEntity<Set<FilmResponseDto>> showMostPopularFilms(@RequestParam(name = "count", defaultValue = "10")
                                                              @Valid @Max(10000)
                                                              @Positive(message = "Count должен быть больше 0")
                                                              int count,
                                                              @RequestParam(name = "genreId", required = false)
                                                              @Valid
                                                              @Positive(message = "genreId должен быть больше 0")
                                                              Integer genreId,
                                                              @RequestParam(name = "year", required = false)
                                                              @Valid
                                                              @Min(value = 1895L, message = "year должен быть не меньше 1895г.")
                                                              Integer year);

    @GetMapping("/search")
    ResponseEntity<List<FilmResponseDto>> searchFilms(@RequestParam(name = "query", defaultValue = "") String query, @RequestParam(defaultValue = "title") String by);


    @GetMapping("/director/{directorId}")
    ResponseEntity<List<FilmResponseDto>> searchFilmsByDirector(@PathVariable Long directorId, @RequestParam(name = "sortBy", defaultValue = "year") String sortBy);

    @PostMapping
    ResponseEntity<FilmResponseDto> createFilm(@Valid @RequestBody FilmRequestDto film);

    @PutMapping
    ResponseEntity<FilmResponseDto> updateFilm(@Valid @RequestBody FilmRequestDto film);

    @PutMapping("/{id}/like/{userId}")
    ResponseEntity<Void> setLikeToSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);

    @DeleteMapping("/{id}/like/{userId}")
    ResponseEntity<Void> removeLikeFromSpecificFilmByUser(@PathVariable Long id, @PathVariable Long userId);

    @GetMapping("/common")
    ResponseEntity<List<FilmResponseDto>> showCommonFilms(@RequestParam Long userId, @RequestParam Long friendId);


    @DeleteMapping("/{id}")
    ResponseEntity<FilmResponseDto> removeFilmById(@PathVariable Long id);
}
