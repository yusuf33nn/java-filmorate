package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public final class FilmMapper {

    private final MpaMapper mpaMapper;

    public FilmResponseDto toDto(Film e) {
        return FilmResponseDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .duration(e.getDuration())
                .releaseDate(e.getReleaseDate())
                .mpa(mpaMapper.toDto(e.getMpa()))
                .genres(e.getGenres())
                .likes(e.getLikes())
                .directors(new HashSet<>(e.getDirectors()))
                .build();
    }

    public Film toEntity(FilmRequestDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpa(mpaMapper.toEntity(dto.getMpa()))
                .likes(dto.getLikes())
                .genres(new HashSet<>(dto.getGenres()))
                .directors(new HashSet<>(dto.getDirectors()))
                .build();
    }
}
