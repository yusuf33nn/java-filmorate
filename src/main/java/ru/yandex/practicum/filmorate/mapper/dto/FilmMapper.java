package ru.yandex.practicum.filmorate.mapper.dto;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.stream.Collectors;

@UtilityClass
public class FilmMapper {

    public FilmResponseDto toDto(Film e) {
        return FilmResponseDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .duration(e.getDuration())
                .releaseDate(e.getReleaseDate())
                .mpa(MpaMapper.toDto(e.getMpa()))
                .genres(e.getGenres().stream().map(GenreMapper::toDto).collect(Collectors.toSet()))
                .directors(e.getDirectors().stream().map(DirectorMapper::toDto).collect(Collectors.toSet()))
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
                .mpa(MpaMapper.toEntity(dto.getMpa()))
                .likes(dto.getLikes())
                .genres(dto.getGenres().stream().map(GenreMapper::toEntity).collect(Collectors.toSet()))
                .directors(dto.getDirectors().stream().map(DirectorMapper::toEntity).collect(Collectors.toSet()))
                .build();
    }
}
