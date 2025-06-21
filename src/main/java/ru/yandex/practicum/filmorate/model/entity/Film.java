package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/**
 * Film.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"likes", "genres"})
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private MpaRating mpa;
    @Builder.Default
    private Set<Long> likes = Collections.emptySet();
    @Builder.Default
    private Set<Genre> genres = Collections.emptySet();
}
