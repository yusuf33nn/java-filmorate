package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor
@Import({FilmDbStorage.class, FilmRowMapper.class})
class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage storage;
    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Taxi 1");
        film.setDescription("Film description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120L);
        film.setMpa(new MpaRating(1, "G", "description"));
        storage.saveFilm(film);
    }

    @Test
    void searchFilms1() {

        List<Film> films = storage.searchFilms("taxi", true);
        assertNotNull(films);
        assertTrue(films.size() > 0);
        assertEquals(1, films.size());
    }
}