package ru.yandex.practicum.filmorate.storage.in_memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.api.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    public List<Film> showMostPopularFilms(Integer count) {
        return films.values().stream()
                .sorted((film1, film2) -> {
                    var film1Size = Optional.ofNullable(film1.getLikes()).map(Set::size).orElse(0);
                    var film2Size = Optional.ofNullable(film2.getLikes()).map(Set::size).orElse(0);
                    return Long.compare(film2Size, film1Size);
                })
                .limit(count)
                .toList();
    }

    @Override
    public void saveFilm(Film film) {
        films.put(film.getId(), film);
    }
}
