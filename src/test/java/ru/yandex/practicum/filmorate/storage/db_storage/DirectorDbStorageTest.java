package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DirectorDbStorage.class, DirectorRowMapper.class})
class DirectorDbStorageTest {

    private final DirectorDbStorage directorStorage;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private FilmDbStorage filmDbStorage;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM directors");
        jdbcTemplate.update("DELETE FROM film_director");
    }

    @Test
    void findAll() {
        Director director1 = directorStorage.saveDirector(new Director(null, "Станислав Ростоцкий"));
        Director director2 = directorStorage.saveDirector(new Director(null, "Леонид Гайдай"));

        List<Director> directors = directorStorage.findAll();

        assertEquals(2, directors.size());
        assertTrue(directors.containsAll(List.of(director1, director2)));
    }

    @Test
    void findDirectorById() {
        Director director = directorStorage.saveDirector(new Director(null, "Станислав Ростоцкий"));

        MpaRating mpa = new MpaRating(1, "G", "description");

        Film film = new Film();
        film.setName("А зори здесь тихие");
        film.setDescription("Драма Ростоцкого");
        film.setReleaseDate(LocalDate.of(1972, 11, 4));
        film.setDuration(160L);
        film.setMpa(mpa);
        film.setDirectors(Set.of(director));
        filmDbStorage.saveFilm(film);

        List<Director> result = directorStorage.findDirectorsByFilmId(1L);

        assertEquals(1, result.size());
        assertEquals(director, result.get(0));
    }

    @Test
    void saveDirector() {
        Director directorNew = new Director(null, "Станислав Ростоцкий");
        Director directorSave = directorStorage.saveDirector(directorNew);

        assertNotNull(directorNew.getId());
        assertEquals(directorSave.getName(), directorNew.getName());

        Director fromDb = jdbcTemplate.queryForObject(
                "SELECT * FROM directors WHERE id = ?",
                (rs, rowNum) -> {
                    Director d = new Director();
                    d.setId(rs.getLong("id"));
                    d.setName(rs.getString("name"));
                    return d;
                },
                directorSave.getId()
        );

        assertEquals(directorSave, fromDb);

    }

    @Test
    void updateDirector() {
        Director director = new Director(null, "Станислав Ростоцкий");
        Director saved = directorStorage.saveDirector(director);

        saved.setName("Леонид Гайдай");

        Director updated = directorStorage.updateDirector(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Леонид Гайдай", updated.getName());

        Director fromDb = directorStorage.findDirectorById(director.getId()).orElseThrow();
        assertEquals(updated, fromDb);
    }

    @Test
    void removeDirector() {
        Director director = new Director(null, "Станислав Ростоцкий");
        Director saved = directorStorage.saveDirector(director);
        directorStorage.removeDirector(saved.getId());

        Optional<Director> found = directorStorage.findDirectorById(saved.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    void findDirectorsByParams() {
        Director d1 = directorStorage.saveDirector(new Director(null, "Станислав Ростоцкий"));
        Director d2 = directorStorage.saveDirector(new Director(null, "Леонид Гайдай"));
        Director d3 = directorStorage.saveDirector(new Director(null, "Егор Луканин"));

        Collection<Director> result = directorStorage.findDirectorsByParams(List.of(d1.getId(), d2.getId()));

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(d1.getId())));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(d2.getId())));
    }

    @Test
    void findDirectorsByDirectorId() {
        Director director = directorStorage.saveDirector(new Director(null, "Станислав Ростоцкий"));

        MpaRating mpa = new MpaRating(1, "G", "description");

        Film film = new Film();
        film.setName("А зори здесь тихие");
        film.setDescription("Драма Ростоцкого");
        film.setReleaseDate(LocalDate.of(1972, 11, 4));
        film.setDuration(160L);
        film.setMpa(mpa);
        film.setDirectors(Set.of(director));
        filmDbStorage.saveFilm(film);

        List<Director> result = directorStorage.findDirectorsByDirectorId(director.getId());

        assertEquals(1, result.size());
        assertEquals(director, result.get(0));
    }

    @Test
    void findDirectorsByFilmId() {
        Director director = directorStorage.saveDirector(new Director(null, "Станислав Ростоцкий"));

        MpaRating mpa = new MpaRating(1, "G", "description");

        Film film = new Film();
        film.setName("А зори здесь тихие");
        film.setDescription("Драма Ростоцкого");
        film.setReleaseDate(LocalDate.of(1972, 11, 4));
        film.setDuration(160L);
        film.setMpa(mpa);
        film.setDirectors(Set.of(director));
        filmDbStorage.saveFilm(film);

        List<Director> result = directorStorage.findDirectorsByFilmId(1L);

        assertEquals(1, result.size());
        assertEquals(director, result.get(0));
    }
}