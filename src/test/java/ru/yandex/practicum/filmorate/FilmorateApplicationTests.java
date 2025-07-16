package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.db_storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class,
        UserRowMapper.class, FilmRowMapper.class})
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;


    @Test
    void testCommonFilms() {
        User user1 = User.builder()
                .name("user1")
                .login("userLogin1")
                .email("test1@test.ru")
                .birthday(LocalDate.of(1980, 10, 25))
                .build();
        user1 = userStorage.saveUser(user1);

        User user2 = User.builder()
                .name("user2")
                .login("userLogin2")
                .email("test2@test.ru")
                .birthday(LocalDate.of(1980, 10, 25))
                .build();
        user2 = userStorage.saveUser(user2);

        Film film1 = Film.builder()
                .name("film1")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film1 = filmStorage.saveFilm(film1);
        Film film2 = Film.builder()
                .name("film2")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film2 = filmStorage.saveFilm(film2);
        Film film3 = Film.builder()
                .name("film3")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film3 = filmStorage.saveFilm(film3);

        filmStorage.setLikeToSpecificFilmByUser(film1.getId(), user1.getId());
        filmStorage.setLikeToSpecificFilmByUser(film2.getId(), user1.getId());

        filmStorage.setLikeToSpecificFilmByUser(film2.getId(), user2.getId());
        filmStorage.setLikeToSpecificFilmByUser(film3.getId(), user2.getId());

        List<Film> filmList = filmStorage.findCommon(user1.getId(), user2.getId());
        assertEquals(1, filmList.size(), "Ожидаемое количество общих фильмов 1");
        assertEquals(filmList.get(0).getName(), film2.getName(), "Имя общего фильма не совпало с " + film2.getName());

    }

}
