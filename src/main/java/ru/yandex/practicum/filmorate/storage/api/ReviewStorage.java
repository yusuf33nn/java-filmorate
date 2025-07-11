package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review saveReview(Review review);

    Review updateReview(Review review);

    Optional<Review> findReviewById(Long reviewId);

    List<Review> findReviewByFilm(Long filmId, Long count);

    void deleteReview(Long reviewId);

    void addReviewLike(Long reviewId, Long userId);

    void addReviewDislike(Long reviewId, Long userId);

    void deleteReviewLike(Long reviewId, Long userId);

    void deleteReviewDislike(Long reviewId, Long userId);
}
