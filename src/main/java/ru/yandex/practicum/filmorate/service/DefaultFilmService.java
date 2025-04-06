package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultFilmService implements FilmService {

    private final AtomicLong filmId = new AtomicLong(0L);
    private final FilmStorage filmStorage;

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    @Override
    public Film findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Film with ID: '%d' is not found".formatted(filmId)));
    }

    @Override
    public List<Film> showMostPopularFilms(Integer count) {
        return filmStorage.showMostPopularFilms(count);
    }

    @Override
    public Film createFilm(Film film) {
        Long filmId = this.filmId.incrementAndGet();
        film.setId(filmId);
        filmStorage.saveFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Long filmId = film.getId();
        if (filmId == null || filmId == 0) {
            log.error("Film id cannot be null or zero for update operation");
            throw new RuntimeException();
        }
        findFilmById(filmId);
        filmStorage.saveFilm(film);
        return film;
    }

    @Override
    public Film setLikeToSpecificFilmByUser(Long filmId, Long userId) {
        Film film = findFilmById(filmId);
        Set<Long> filmLikes = film.getLikes();
        if (filmLikes == null) {
            filmLikes = new TreeSet<>();
        }
        filmLikes.add(userId);
        return film;
    }

    @Override
    public Film removeLikeFromSpecificFilmByUser(Long filmId, Long userId) {
        Film film = findFilmById(filmId);
        Set<Long> filmLikes = film.getLikes();
        if (filmLikes == null || filmLikes.isEmpty()) {
            var errorMessage = "You can't remove like, if the film doesn't have any likes";
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        filmLikes.remove(userId);
        return film;
    }
}
