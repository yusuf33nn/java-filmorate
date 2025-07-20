package ru.yandex.practicum.filmorate.mapper.dto;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;

@UtilityClass
public class MpaMapper {

    public MpaDto toDto(MpaRating e) {
        return MpaDto.builder()
                .id(e.getId())
                .name(e.getRatingCode())
                .build();
    }

    public MpaRating toEntity(MpaDto dto) {
        return MpaRating.builder()
                .id(dto.getId())
                .ratingCode(dto.getName())
                .build();
    }
}
