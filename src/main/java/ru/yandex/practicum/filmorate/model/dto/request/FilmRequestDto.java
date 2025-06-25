package ru.yandex.practicum.filmorate.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.annotation.MinDate;
import ru.yandex.practicum.filmorate.annotation.PositiveDuration;
import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;
import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmRequestDto {

    @Positive
    Long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotNull
    @MinDate(min = "1895-12-28", message = "Дата должна быть не меньше 28 декабря 1895г.")
    LocalDate releaseDate;
    @NotNull
    @PositiveDuration
    Long duration;
    MpaDto mpa;
    Set<Genre> genres = Collections.emptySet();
}
