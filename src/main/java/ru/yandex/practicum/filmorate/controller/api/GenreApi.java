package ru.yandex.practicum.filmorate.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.util.List;

@RequestMapping(value = "/genres")
public interface GenreApi {

    @GetMapping
    ResponseEntity<List<Genre>> showAllGenres();

    @GetMapping("/{id}")
    ResponseEntity<Genre> findGenreById(@PathVariable Integer id);
}
