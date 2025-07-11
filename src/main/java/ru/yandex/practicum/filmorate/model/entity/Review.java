package ru.yandex.practicum.filmorate.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    Long id;
    String content;
    Boolean isPositive;
    Long userId;
    Long filmId;
    @Builder.Default
    Integer useful = 0;
}
