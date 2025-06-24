package ru.yandex.practicum.filmorate.model.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmResponseDto {

    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Long duration;
    MpaDto mpa;
    @Builder.Default
    Set<Long> likes = Collections.emptySet();
    @Builder.Default
    Set<Genre> genres = Collections.emptySet();
}
