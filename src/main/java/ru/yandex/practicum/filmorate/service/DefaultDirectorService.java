package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.dto.DirectorMapper;
import ru.yandex.practicum.filmorate.model.dto.request.DirectorRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.DirectorResponseDto;
import ru.yandex.practicum.filmorate.service.api.DirectorService;
import ru.yandex.practicum.filmorate.storage.db_storage.DirectorDbStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultDirectorService implements DirectorService {

    private final DirectorDbStorage directorDbStorage;

    @Override
    public List<DirectorResponseDto> showAllDirectors() {
        return directorDbStorage.findAll().stream()
                .map(DirectorMapper::toDto)
                .toList();
    }

    @Override
    public DirectorResponseDto findDirectorById(Long id) {
        if (id == null) {
            return directorDbStorage.findAll().stream().findFirst().map(DirectorMapper::toDto)
                    .orElseThrow(() -> new NotFoundException("Director not found"));
        }
        return directorDbStorage.findDirectorById(id)
                .map(DirectorMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Director with id: '%d' not found".formatted(id)));
    }

    @Override
    public DirectorResponseDto createDirector(DirectorRequestDto director) {
        var directorEntity = directorDbStorage.saveDirector(DirectorMapper.toEntity(director));
        return DirectorMapper.toDto(directorEntity);
    }

    @Override
    public DirectorResponseDto updateDirector(DirectorRequestDto director) {
        Long directorId = director.getId();
        if (directorId == null || directorId == 0) {
            throw new ValidationException("Director id cannot be null or zero for update operation");
        }
        findDirectorById(directorId);
        var updatedDirector = directorDbStorage.updateDirector(DirectorMapper.toEntity(director));
        if (updatedDirector == null) {
            throw new RuntimeException("Error while updating Director with ID: %d".formatted(directorId));
        }
        return findDirectorById(directorId);
    }

    @Override
    public void removeDirector(Long id) {
        directorDbStorage.removeDirector(id);
    }

    @Override
    public Set<DirectorResponseDto> findDirectorsByDirectorId(Long directorId) {
        return directorDbStorage.findDirectorsByDirectorId(directorId).stream().map(DirectorMapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<DirectorResponseDto> findDirectorsByFilmId(Long filmId) {
        return directorDbStorage.findDirectorsByFilmId(filmId).stream().map(DirectorMapper::toDto).collect(Collectors.toSet());
    }
}
