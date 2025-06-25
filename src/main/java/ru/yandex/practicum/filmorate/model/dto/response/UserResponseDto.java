package ru.yandex.practicum.filmorate.model.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {

    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    @Builder.Default
    Set<Long> friends = Collections.emptySet();
}
