package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.request.DirectorRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.DirectorResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Director;

@Component
@RequiredArgsConstructor
public class DirectorMapper {

    private final MpaMapper mpaMapper;

    public DirectorResponseDto toDto(Director director) {
        return DirectorResponseDto.builder()
                .id(director.getId())
                .name(director.getName())
                .build();
    }

    public Director toDto(DirectorResponseDto directorResponseDto) {
        return Director.builder()
                .id(directorResponseDto.getId())
                .name(directorResponseDto.getName())
                .build();
    }

    public Director toEntity(DirectorRequestDto dto) {
        return Director.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
