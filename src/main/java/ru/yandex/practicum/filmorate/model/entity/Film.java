package ru.yandex.practicum.filmorate.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Long duration;
    MpaRating mpa;
    @Builder.Default
    Set<Long> likes = Collections.emptySet();
    @Builder.Default
    Set<Genre> genres = Collections.emptySet();
}
