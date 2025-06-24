package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    @Qualifier(value = "userDbStorage")
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> showAllUsers() {
        return userStorage.showAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto findUserById(Long userId) {
        return userStorage.findUserById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    var errorMessage = "User with ID: '%d' is not found".formatted(userId);
                    log.error(errorMessage);
                    return new NotFoundException(errorMessage);
                });
    }


    @Override
    public UserResponseDto createUser(UserRequestDto user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        var createdUser = userStorage.saveUser(userMapper.toEntity(user));
        return userMapper.toDto(createdUser);
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto user) {
        Long userId = user.getId();
        if (userId == null || userId == 0) {
            var errorMessage = "User id cannot be null or zero for update operation";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        findUserById(userId);
        var updatedRows = userStorage.updateUser(userMapper.toEntity(user));
        if (updatedRows != 1) {
            throw new RuntimeException("Error while updating user with ID: " + userId);
        }
        return findUserById(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }
}
