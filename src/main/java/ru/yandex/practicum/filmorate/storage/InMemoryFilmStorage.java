package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                .sorted(Comparator.comparing(film -> ((Film) film).getLikes().size()).reversed())
                .limit(count)
                .toList();
    }

    @Override
    public void saveFilm(Film film) {
        films.put(film.getId(), film);
    }
}
