package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.dto.request.ReviewRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.ReviewResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.service.api.FilmService;
import ru.yandex.practicum.filmorate.service.api.ReviewService;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.ReviewStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    @Qualifier(value = "reviewDbStorage")
    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto ReviewDto) {
        Review reviewEntity = reviewMapper.toEntity(ReviewDto);
        userService.findUserById(reviewEntity.getUserId());
        filmService.findFilmById(reviewEntity.getFilmId());
        reviewEntity = reviewStorage.saveReview(reviewEntity);
        final var savedId = reviewEntity.getId();
        return reviewMapper.toDto(reviewEntity);
    }

    @Override
    public ReviewResponseDto updateReview(ReviewRequestDto reviewDto) {
        Long reviewId = reviewDto.getReviewId();
        if (reviewId == null || reviewId == 0) {
            log.error("Review id cannot be null or zero for update operation");
            throw new RuntimeException();
        }
        findReviewById(reviewId);
        Review review = reviewStorage.updateReview(reviewMapper.toEntity(reviewDto));
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewResponseDto findReviewById(Long reviewId) {
        ReviewResponseDto responseDto = reviewStorage.findReviewById(reviewId)
                .map(reviewMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Review with ID: '%d' is not found".formatted(reviewId)));
        return responseDto;
    }

    @Override
    public List<ReviewResponseDto> findReviewByFilm(Long filmId, Long count) {
        return reviewStorage.findReviewByFilm(filmId, count).stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewStorage.deleteReview(reviewId);
    }

    @Override
    public void addReviewLike(Long reviewId, Long userId) {

        if (reviewId == null || reviewId == 0) {
            log.error("Review id cannot be null or zero for addReviewLike operation");
            throw new RuntimeException();
        }
        if (userId == null || userId == 0) {
            log.error("UserId cannot be null or zero for addReviewLike operation");
            throw new RuntimeException();
        }
        userService.findUserById(userId);
        findReviewById(reviewId);
        reviewStorage.addReviewLike(reviewId, userId);
    }

    @Override
    public void addReviewDislike(Long reviewId, Long userId) {

        if (reviewId == null || reviewId == 0) {
            log.error("Review id cannot be null or zero for addReviewDislike operation");
            throw new RuntimeException();
        }
        if (userId == null || userId == 0) {
            log.error("UserId cannot be null or zero for addReviewDislike operation");
            throw new RuntimeException();
        }
        userService.findUserById(userId);
        findReviewById(reviewId);
        reviewStorage.addReviewDislike(reviewId, userId);
    }

    @Override
    public void deleteReviewLike(Long reviewId, Long userId) {
        if (reviewId == null || reviewId == 0) {
            log.error("Review id cannot be null or zero for deleteReviewLike operation");
            throw new RuntimeException();
        }
        if (userId == null || userId == 0) {
            log.error("UserId cannot be null or zero for deleteReviewLike operation");
            throw new RuntimeException();
        }
        userService.findUserById(userId);
        findReviewById(reviewId);
        reviewStorage.deleteReviewLike(reviewId, userId);
    }

    @Override
    public void deleteReviewDislike(Long reviewId, Long userId) {
        if (reviewId == null || reviewId == 0) {
            log.error("Review id cannot be null or zero for deleteReviewDislike operation");
            throw new RuntimeException();
        }
        if (userId == null || userId == 0) {
            log.error("UserId cannot be null or zero for deleteReviewDislike operation");
            throw new RuntimeException();
        }
        userService.findUserById(userId);
        findReviewById(reviewId);
        reviewStorage.deleteReviewDislike(reviewId, userId);
    }
}
