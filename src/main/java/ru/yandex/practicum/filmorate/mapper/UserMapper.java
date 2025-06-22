package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.model.entity.User;

@Component
public final class UserMapper {

    public UserResponseDto toDto(User e) {
        return UserResponseDto.builder()
                .id(e.getId())
                .login(e.getLogin())
                .email(e.getEmail())
                .name(e.getName())
                .birthday(e.getBirthday())
                .build();
    }

    public User toEntity(UserRequestDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .build();
    }
}
