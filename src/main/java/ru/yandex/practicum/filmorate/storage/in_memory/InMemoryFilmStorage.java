package ru.yandex.practicum.filmorate.storage.in_memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Set<Film> showMostPopularFilms(Integer count) {
        return films.values().stream()
                .sorted((film1, film2) -> {
                    var film1Size = Optional.ofNullable(film1.getLikes()).map(Set::size).orElse(0);
                    var film2Size = Optional.ofNullable(film2.getLikes()).map(Set::size).orElse(0);
                    return Long.compare(film2Size, film1Size);
                })
                .limit(count)
                .collect(Collectors.toSet());
    }

    @Override
    public Film saveFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public void setLikeToSpecificFilmByUser(Long filmId, Long userId) {

    }

    @Override
    public void removeLikeFromSpecificFilmByUser(Long filmId, Long userId) {

    }

    @Override
    public Set<Long> getFilmLikesByFilmId(Long filmId) {
        return Set.of();
    }
}
