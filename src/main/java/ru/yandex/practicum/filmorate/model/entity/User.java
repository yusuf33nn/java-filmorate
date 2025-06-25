package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"friends"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    @Builder.Default
    Set<Long> friends = Collections.emptySet();
}
