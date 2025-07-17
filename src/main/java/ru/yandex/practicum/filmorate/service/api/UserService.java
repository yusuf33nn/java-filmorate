package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.List;

public interface UserService {

    List<UserResponseDto> showAllUsers();

    UserResponseDto findUserById(Long userId);

    UserResponseDto createUser(UserRequestDto user);

    UserResponseDto updateUser(UserRequestDto user);

    void deleteUser(Long userId);

    List<Film> getRecommendations(Long userId);

    void removeUserById(Long userId);
}
