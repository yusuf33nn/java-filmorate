package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.request.ReviewRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewRequestDto ReviewDto);

    ReviewResponseDto updateReview(ReviewRequestDto reviewDto);

    ReviewResponseDto findReviewById(Long reviewId);

    List<ReviewResponseDto> findReviewByFilm (Long filmId, Long count);
}
