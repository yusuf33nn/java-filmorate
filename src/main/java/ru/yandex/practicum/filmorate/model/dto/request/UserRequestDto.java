package ru.yandex.practicum.filmorate.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserRequestDto {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
