package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.GenreApi;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.service.api.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController implements GenreApi {

    private final GenreService genreService;

    @Override
    public ResponseEntity<List<Genre>> showAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @Override
    public ResponseEntity<Genre> findGenreById(Integer id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }
}
