package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.GenreMapper;
import ru.yandex.practicum.filmorate.model.dto.response.GenreDto;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.service.api.GenreService;
import ru.yandex.practicum.filmorate.storage.db_storage.GenreDbStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {

    private final GenreDbStorage genreDbStorage;

    @Override
    public List<GenreDto> getAllGenres() {
        return genreDbStorage.getAllGenres().stream().map(GenreMapper::toDto).toList();
    }

    @Override
    public GenreDto getGenreById(Integer id) {
        return genreDbStorage.getGenreById(id).map(GenreMapper::toDto).orElseThrow(
                () -> new NotFoundException("Genre with id " + id + " not found")
        );
    }

    @Override
    public void addGenreToFilm(Integer genreId, Long filmId) {
        genreDbStorage.addGenreToFilm(genreId, filmId);
    }

    @Override
    public Set<Genre> getGenresByFilmId(Long filmId) {
        return genreDbStorage.getGenresByFilmId(filmId);
    }
}
