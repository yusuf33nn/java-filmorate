package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.MpaApi;
import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;
import ru.yandex.practicum.filmorate.service.api.MpaRatingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MpaController implements MpaApi {

    private final MpaRatingService mpaRatingService;

    @Override
    public ResponseEntity<List<MpaDto>> showAllMpaRatings() {
        return ResponseEntity.ok(mpaRatingService.getAllMpaRatings());
    }

    @Override
    public ResponseEntity<MpaDto> findMpaRatingById(Integer id) {
        return ResponseEntity.ok(mpaRatingService.getMpaRatingById(id));
    }
}
