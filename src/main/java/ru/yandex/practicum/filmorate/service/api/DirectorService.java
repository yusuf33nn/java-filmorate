package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.request.DirectorRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.DirectorResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Director;

import java.util.List;
import java.util.Set;

public interface DirectorService {

    List<DirectorResponseDto> showAllDirectors();

    DirectorResponseDto findDirectorById(Long directorId);

    DirectorResponseDto createDirector(DirectorRequestDto director);

    DirectorResponseDto updateDirector(DirectorRequestDto director);

    void removeDirector(Long id);

    Set<Director> findDirectorsByDirectorId(Long filmId);

    Set<Director> findDirectorsByFilmId(Long filmId);
}
