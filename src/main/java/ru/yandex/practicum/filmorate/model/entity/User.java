package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

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
public class User {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = Collections.emptySet();
}
