package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.Director;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> findAll();

    Optional<Director> findDirectorById(Long id);

    Director saveDirector(Director director);

    Director updateDirector(Director director);

    void removeDirector(Long id);

    Collection<Director> findDirectorsByParams(Collection<Long> params);

    List<Director> findDirectorsByDirectorId(Long directorId);

    List<Director> findDirectorsByFilmId(Long filmId);
}
