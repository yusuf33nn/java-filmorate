package ru.yandex.practicum.filmorate.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {

    @Positive
    Long id;
    @Email
    @NotBlank
    String email;
    @NotBlank
    String login;
    String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}
