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
import ru.yandex.practicum.filmorate.model.dto.request.DirectorRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.DirectorResponseDto;

import java.util.List;

@RequestMapping(value = "/directors")
public interface DirectorApi {

    @GetMapping
    ResponseEntity<List<DirectorResponseDto>> showAllDirectors();

    @GetMapping("/{directorId}")
    ResponseEntity<DirectorResponseDto> findDirectorById(@PathVariable Long directorId);

    @PostMapping
    ResponseEntity<DirectorResponseDto> createDirector(@Valid @RequestBody DirectorRequestDto film);

    @PutMapping
    ResponseEntity<DirectorResponseDto> updateDirector(@Valid @RequestBody DirectorRequestDto film);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> removeDirector(@PathVariable Long id);
}
