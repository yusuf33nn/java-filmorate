package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.request.ReviewRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.ReviewResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Review;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    public ReviewResponseDto toDto(Review e) {
        return ReviewResponseDto.builder()
                .reviewId(e.getId())
                .content(e.getContent())
                .isPositive(e.getIsPositive())
                .userId(e.getUserId())
                .filmId(e.getFilmId())
                .useful(e.getUseful())
                .build();
    }

    public Review toEntity(ReviewRequestDto dto) {
        return Review.builder()
                .id(dto.getReviewId())
                .content(dto.getContent())
                .isPositive(dto.getIsPositive())
                .filmId(dto.getFilmId())
                .userId(dto.getUserId())
                .useful(dto.getIseful())
                .build();
    }
}
