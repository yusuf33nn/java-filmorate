package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.DirectorApi;
import ru.yandex.practicum.filmorate.model.dto.request.DirectorRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.DirectorResponseDto;
import ru.yandex.practicum.filmorate.service.api.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DirectorController implements DirectorApi {

    private final DirectorService directorService;

    @Override
    public ResponseEntity<List<DirectorResponseDto>> showAllDirectors() {
        return ResponseEntity.ok(directorService.showAllDirectors());
    }

    @Override
    public ResponseEntity<DirectorResponseDto> findDirectorById(Long directorId) {
        return ResponseEntity.ok(directorService.findDirectorById(directorId));
    }

    @Override
    public ResponseEntity<DirectorResponseDto> createDirector(DirectorRequestDto director) {
        log.info("Create director: {}", director);
        return ResponseEntity.ok(directorService.createDirector(director));
    }

    @Override
    public ResponseEntity<DirectorResponseDto> updateDirector(DirectorRequestDto director) {
        return ResponseEntity.ok(directorService.updateDirector(director));
    }

    @Override
    public ResponseEntity<Void> removeDirector(Long directorId) {
        directorService.removeDirector(directorId);
        return ResponseEntity.ok().build();
    }
}
