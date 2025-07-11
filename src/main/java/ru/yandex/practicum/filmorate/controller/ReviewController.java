package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.ReviewApi;
import ru.yandex.practicum.filmorate.model.dto.request.ReviewRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.ReviewResponseDto;
import ru.yandex.practicum.filmorate.service.DefaultReviewService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewApi {
    private final DefaultReviewService reviewService;

    @Override
    public ResponseEntity<ReviewResponseDto> createReview(ReviewRequestDto review) {
        log.info("CreateReview Request Body: {}", review);
        return ResponseEntity.status(CREATED).body(reviewService.createReview(review));
    }

    @Override
    public ResponseEntity<ReviewResponseDto> updateReview(ReviewRequestDto review) {
        log.info("UpdateReview Request Body: {}", review);
        return ResponseEntity.ok(reviewService.updateReview(review));
    }

    @Override
    public ResponseEntity<ReviewResponseDto> findReviewById(Long id) {
        return ResponseEntity.ok(reviewService.findReviewById(id));
    }

    @Override
    public ResponseEntity<List<ReviewResponseDto>> findReviewByFilm(Long filmId, Long count) {
        return ResponseEntity.ok(reviewService.findReviewByFilm(filmId, count));
    }

    @Override
    public ResponseEntity<Void> deleteReview(Long id) {
       reviewService.deleteReview(id);
       return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addReviewLike(Long reviewId, Long userId) {
        reviewService.addReviewLike(reviewId, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addReviewDislike(Long reviewId, Long userId) {
        reviewService.addReviewDislike(reviewId, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteReviewLike(Long reviewId, Long userId) {
        reviewService.deleteReviewLike(reviewId, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> DeleteReviewDislike(Long reviewId, Long userId) {
        reviewService.deleteReviewDislike(reviewId, userId);
        return ResponseEntity.ok().build();
    }

}
