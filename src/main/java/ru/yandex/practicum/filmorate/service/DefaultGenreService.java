package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.service.api.GenreService;
import ru.yandex.practicum.filmorate.storage.db_storage.GenreDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {

    private final GenreDbStorage genreDbStorage;

    @Override
    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genreDbStorage.getGenreById(id).orElseThrow(
                () -> new RuntimeException("Genre with id " + id + " not found")
        );
    }

    @Override
    public void addGenreToFilm(Integer genreId, Long filmId) {
        genreDbStorage.addGenreToFilm(genreId, filmId);
    }
}
