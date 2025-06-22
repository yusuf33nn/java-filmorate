package ru.yandex.practicum.filmorate.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinDate;
import ru.yandex.practicum.filmorate.annotation.PositiveDuration;
import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;
import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmRequestDto {

    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @MinDate(min = "1895-12-28", message = "Дата должна быть не меньше 28 декабря 1895г.")
    private LocalDate releaseDate;
    @NotNull
    @PositiveDuration
    private Long duration;
    private MpaDto mpa;
    private Set<Genre> genres = Collections.emptySet();
}
