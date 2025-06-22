package ru.yandex.practicum.filmorate.model.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class FilmResponseDto {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private MpaDto mpa;
    @Builder.Default
    private Set<Long> likes = Collections.emptySet();
    @Builder.Default
    private Set<Genre> genres = Collections.emptySet();
}
