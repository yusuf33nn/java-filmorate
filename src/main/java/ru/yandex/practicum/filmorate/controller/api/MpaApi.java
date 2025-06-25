package ru.yandex.practicum.filmorate.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;

import java.util.List;

@RequestMapping(value = "/mpa")
public interface MpaApi {

    @GetMapping
    ResponseEntity<List<MpaDto>> showAllMpaRatings();

    @GetMapping("/{id}")
    ResponseEntity<MpaDto> findMpaRatingById(@PathVariable Integer id);
}
