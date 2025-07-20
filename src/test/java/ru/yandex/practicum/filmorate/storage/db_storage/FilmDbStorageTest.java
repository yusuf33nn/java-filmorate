package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.row.DirectorRowMapper;
import ru.yandex.practicum.filmorate.mapper.row.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class, DirectorDbStorage.class, DirectorRowMapper.class})
class FilmDbStorageTest {


    private final FilmDbStorage filmDbStorage;
    private final DirectorDbStorage directorDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM film_like");
        jdbcTemplate.update("DELETE FROM film_director");
        jdbcTemplate.update("DELETE FROM film_genre");
        jdbcTemplate.update("DELETE FROM film");
        jdbcTemplate.update("DELETE FROM directors");

        Director director1 = new Director();
        director1.setName("Станислав Ростоцкий");
        Director savedDirector1 = directorDbStorage.saveDirector(director1);

        Director director2 = new Director();
        director2.setName("Леонид Гайдай");
        Director savedDirector2 = directorDbStorage.saveDirector(director2);

        MpaRating mpa = new MpaRating(1, "G", "description");

        Film film1 = new Film();
        film1.setName("Бриллиантовая рука");
        film1.setDescription("Комедия Леонида Гайдая");
        film1.setReleaseDate(LocalDate.of(1969, 4, 28));
        film1.setDuration(94L);
        film1.setMpa(mpa);
        film1.setDirectors(Set.of(savedDirector2));
        filmDbStorage.saveFilm(film1);

        Film film2 = new Film();
        film2.setName("Операция „Ы“ и другие приключения Шурика");
        film2.setDescription("Еще одна комедия Гайдая");
        film2.setReleaseDate(LocalDate.of(1965, 7, 23));
        film2.setDuration(90L);
        film2.setMpa(mpa);
        film2.setDirectors(Set.of(savedDirector2));
        filmDbStorage.saveFilm(film2);

        Film film3 = new Film();
        film3.setName("А зори здесь тихие");
        film3.setDescription("Драма Ростоцкого");
        film3.setReleaseDate(LocalDate.of(1972, 11, 4));
        film3.setDuration(160L);
        film3.setMpa(mpa);
        film3.setDirectors(Set.of(savedDirector1));
        filmDbStorage.saveFilm(film3);
    }

    @Test
    void searchFilmsTitleDirector() {
        // Поиск по названию и режиссеру (должен найти "Бриллиантовая рука")
        List<Film> films = filmDbStorage.searchFilms("рука", true, true);

        assertEquals(1, films.size());
        assertEquals("Бриллиантовая рука", films.get(0).getName());
    }

    @Test
    void searchFilmsTitle() {
        // Поиск только по названию
        List<Film> films = filmDbStorage.searchFilms("операция", true, false);

        assertEquals(1, films.size());
        assertEquals("Операция „Ы“ и другие приключения Шурика", films.get(0).getName());
    }

    @Test
    void searchFilmsDirector() {
        // Поиск только по режиссеру (должен найти 2 фильма Гайдая)
        List<Film> films = filmDbStorage.searchFilms("Гайдай", false, true);

        assertEquals(2, films.size());
        assertTrue(films.stream().anyMatch(f -> f.getName().equals("Бриллиантовая рука")));
        assertTrue(films.stream().anyMatch(f -> f.getName().equals("Операция „Ы“ и другие приключения Шурика")));
    }

    @Test
    void searchFilmsNoResults() {
        // Поиск, который не должен ничего найти
        List<Film> films = filmDbStorage.searchFilms("несуществующий запрос", true, true);

        assertTrue(CollectionUtils.isEmpty(films));
    }
}
