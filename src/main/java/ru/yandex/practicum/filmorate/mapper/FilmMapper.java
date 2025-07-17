package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class FilmMapper {

    private final MpaMapper mpaMapper;
    private final DirectorMapper directorMapper;

    public FilmResponseDto toDto(Film e) {
        return FilmResponseDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .duration(e.getDuration())
                .releaseDate(e.getReleaseDate())
                .mpa(mpaMapper.toDto(e.getMpa()))
                .genres(e.getGenres())
                .directors(e.getDirectors().stream().map(directorMapper::toDto).collect(Collectors.toSet()))
                .likes(e.getLikes())
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
                .directors(dto.getDirectors().stream().map(directorMapper::toEntity).collect(Collectors.toSet()))
                .build();
    }
}
