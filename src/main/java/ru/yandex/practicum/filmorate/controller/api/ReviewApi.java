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
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.dto.request.ReviewRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.ReviewResponseDto;

import java.util.Set;

@RequestMapping(value = "/reviews")
public interface ReviewApi {
    @PostMapping
    ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewRequestDto review);

    @PutMapping
    ResponseEntity<ReviewResponseDto> updateReview(@Valid @RequestBody ReviewRequestDto review);

    @GetMapping("/{id}")
    ResponseEntity<ReviewResponseDto> findReviewById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<Set<ReviewResponseDto>> findReviewByFilm(@RequestParam(defaultValue = "-1") Long filmId, @RequestParam(defaultValue = "10") Long count);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReview(@PathVariable Long id);

    @PutMapping("/{reviewId}/like/{userId}")
    ResponseEntity<Void> addReviewLike(@PathVariable Long reviewId, @PathVariable Long userId);

    @PutMapping("/{reviewId}/dislike/{userId}")
    ResponseEntity<Void> addReviewDislike(@PathVariable Long reviewId, @PathVariable Long userId);

    @DeleteMapping("/{reviewId}/like/{userId}")
    ResponseEntity<Void> deleteReviewLike(@PathVariable Long reviewId, @PathVariable Long userId);

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    ResponseEntity<Void> deleteReviewDislike(@PathVariable Long reviewId, @PathVariable Long userId);
}
