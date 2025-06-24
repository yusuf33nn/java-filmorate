package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Genre {

    Integer id;
    String name;
}
