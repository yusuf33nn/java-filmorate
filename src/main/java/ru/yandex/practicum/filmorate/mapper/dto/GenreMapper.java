package ru.yandex.practicum.filmorate.mapper.dto;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.dto.response.GenreDto;
import ru.yandex.practicum.filmorate.model.entity.Genre;

@UtilityClass
public class GenreMapper {

    public GenreDto toDto(Genre e) {
        return GenreDto.builder()
                .id(e.getId())
                .name(e.getName())
                .build();
    }

    public Genre toEntity(GenreDto dto) {
        return Genre.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
