package ru.yandex.practicum.filmorate.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.dto.response.EventType;
import ru.yandex.practicum.filmorate.model.dto.response.Operation;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"eventId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEventFeed {

    Long eventId;

    Long userId;

    EventType eventType;

    Operation operation;

    Long entityId;

    LocalDate timestamp;
}