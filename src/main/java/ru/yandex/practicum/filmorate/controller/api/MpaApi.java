package ru.yandex.practicum.filmorate.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;

import java.util.List;

@RequestMapping(value = "/mpa")
public interface MpaApi {

    @GetMapping
    ResponseEntity<List<MpaRating>> showAllMpaRatings();

    @GetMapping("/{id}")
    ResponseEntity<MpaRating> findMpaRatingById(@PathVariable Integer id);
}
