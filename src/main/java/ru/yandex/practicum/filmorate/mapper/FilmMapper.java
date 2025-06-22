package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Film;

@Component
public final class FilmMapper {

    public FilmResponseDto toDto(Film e) {
        return FilmResponseDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .duration(e.getDuration())
                .releaseDate(e.getReleaseDate())
                .mpa(e.getMpa())
                .genres(e.getGenres())
                .build();
    }

    public Film toEntity(FilmRequestDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpa(dto.getMpa())
                .build();
    }
}
