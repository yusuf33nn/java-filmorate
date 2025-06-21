package ru.yandex.practicum.filmorate.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = Collections.emptySet();
}
