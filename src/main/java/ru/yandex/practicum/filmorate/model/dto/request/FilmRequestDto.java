package ru.yandex.practicum.filmorate.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinDate;
import ru.yandex.practicum.filmorate.annotation.PositiveDuration;

import java.time.LocalDate;

@Data
public class FilmRequestDto {

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
}
