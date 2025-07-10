package ru.yandex.practicum.filmorate.model.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponseDto {
    Long reviewId;
    String content;
    Boolean isPositive;
    Long userId;
    Long filmId;
}

