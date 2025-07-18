package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.ReviewMapper;
import ru.yandex.practicum.filmorate.model.dto.request.ReviewRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.EventType;
import ru.yandex.practicum.filmorate.model.dto.response.Operation;
import ru.yandex.practicum.filmorate.model.dto.response.ReviewResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;
import ru.yandex.practicum.filmorate.service.api.FilmService;
import ru.yandex.practicum.filmorate.service.api.ReviewService;
import ru.yandex.practicum.filmorate.service.api.UserEventFeedService;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.ReviewStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    @Qualifier(value = "reviewDbStorage")
    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;
    private final ReviewMapper reviewMapper;
    private final UserEventFeedService userEventFeedService;

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto reviewDto) {
        Review reviewEntity = reviewMapper.toEntity(reviewDto);
        userService.findUserById(reviewEntity.getUserId());
        filmService.findFilmById(reviewEntity.getFilmId());
        reviewEntity = reviewStorage.saveReview(reviewEntity);
        userEventFeedService.saveEvent(createReviewEvent(reviewEntity, Operation.ADD));
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
        userEventFeedService.saveEvent(createReviewEvent(review, Operation.UPDATE));
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewResponseDto findReviewById(Long reviewId) {
        return reviewStorage.findReviewById(reviewId)
                .map(reviewMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Review with ID: '%d' is not found".formatted(reviewId)));
    }

    @Override
    public LinkedHashSet<ReviewResponseDto> findReviewByFilm(Long filmId, Long count) {
        return reviewStorage.findReviewByFilm(filmId, count).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewStorage.findReviewById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review with ID: '%d' is not found".formatted(reviewId)));
        var event = createReviewEvent(review, Operation.REMOVE);
        reviewStorage.deleteReview(reviewId);
        userEventFeedService.saveEvent(event);
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

    private UserEventFeed createReviewEvent(Review review, Operation operation) {
        return UserEventFeed.builder()
                .userId(review.getUserId())
                .entityId(review.getId())
                .eventType(EventType.REVIEW)
                .operation(operation)
                .timestamp(LocalDate.now())
                .build();
    }
}
