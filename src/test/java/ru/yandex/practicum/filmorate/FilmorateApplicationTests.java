package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.db_storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.db_storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({ReviewDbStorage.class, UserDbStorage.class, FilmDbStorage.class,
        ReviewRowMapper.class, UserRowMapper.class, FilmRowMapper.class})
class FilmorateApplicationTests {

    private final ReviewDbStorage reviewStorage;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testCreateUpdateDeleteReview() {
        User user = User.builder()
                .name("user1")
                .login("userLogin1")
                .email("test@test.ru")
                .birthday(LocalDate.of(1980, 10, 25))
                .build();
        user = userStorage.saveUser(user);

        Film film = Film.builder()
                .name("film1")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film = filmStorage.saveFilm(film);

        Review review = Review.builder()
                .filmId(film.getId())
                .userId(user.getId())
                .content("good film")
                .isPositive(true)
                .build();

        Review newReview = reviewStorage.saveReview(review);
        review.setId(newReview.getId());

        assertNotNull(newReview, "После создания отзыва: Отзыв не найден.");

        assertEquals(review, newReview, "После создания отзыва: Отзывы не совпадают.");

        newReview.setContent("bad film");
        newReview.setIsPositive(false);

        Review updatedReview = reviewStorage.updateReview(newReview);

        assertEquals(newReview.getId(), updatedReview.getId(),
                "После обновления отзыва: ИД обновленого отзыва не равно исходному ИД.");

        assertEquals(newReview.getContent(), updatedReview.getContent(),
                "После обновления отзыва: поле Content не равно 'bad film'");

        reviewStorage.deleteReview(updatedReview.getId());

        Optional<Review> deletedReview = reviewStorage.findReviewById(updatedReview.getId());

        assertEquals(Optional.empty(), deletedReview, "После удаления отзыва: Отзыв найден.");
    }

    @Test
    public void testFindReviewByFilm() {
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

        Film film = Film.builder()
                .name("film1")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film = filmStorage.saveFilm(film);

        Review review1 = Review.builder()
                .filmId(film.getId())
                .userId(user1.getId())
                .content("good film")
                .isPositive(true)
                .build();
        Review newReview1 = reviewStorage.saveReview(review1);

        Review review2 = Review.builder()
                .filmId(film.getId())
                .userId(user1.getId())
                .content("bad film")
                .isPositive(false)
                .build();
        Review newReview2 = reviewStorage.saveReview(review2);

        List<Review> reviewList = reviewStorage.findReviewByFilm(film.getId(), 10L);
        assertEquals(2, reviewList.size(), "Ожидаемое количество отзывов 2");
    }

    @Test
    public void testAddDeleteLike() {

        Film film = Film.builder()
                .name("film1")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film = filmStorage.saveFilm(film);
        User user1 = User.builder()
                .name("user1")
                .login("userLogin1")
                .email("test1@test.ru")
                .birthday(LocalDate.of(1980, 10, 25))
                .build();
        user1 = userStorage.saveUser(user1);

        Review review1 = Review.builder()
                .filmId(film.getId())
                .userId(user1.getId())
                .content("good film")
                .isPositive(true)
                .build();
        Review newReview1 = reviewStorage.saveReview(review1);

        reviewStorage.addReviewLike(review1.getId(), user1.getId());
        Integer useful = reviewStorage.findReviewById(newReview1.getId()).get().getUseful();

        assertEquals(1, useful, "Ожидаемое значение рейтинга после лайка 1");

        reviewStorage.deleteReviewLike(review1.getId(), user1.getId());
        useful = reviewStorage.findReviewById(newReview1.getId()).get().getUseful();

        assertEquals(0, useful, "Ожидаемое значение рейтинга после удаления лайка  0");
    }

    @Test
    public void testAddDeleteDislike() {

        Film film = Film.builder()
                .name("film1")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film = filmStorage.saveFilm(film);
        User user1 = User.builder()
                .name("user1")
                .login("userLogin1")
                .email("test1@test.ru")
                .birthday(LocalDate.of(1980, 10, 25))
                .build();
        user1 = userStorage.saveUser(user1);

        Review review1 = Review.builder()
                .filmId(film.getId())
                .userId(user1.getId())
                .content("good film")
                .isPositive(true)
                .build();
        Review newReview1 = reviewStorage.saveReview(review1);

        reviewStorage.addReviewDislike(review1.getId(), user1.getId());
        Integer useful = reviewStorage.findReviewById(newReview1.getId()).get().getUseful();

        assertEquals(-1, useful, "Ожидаемое значение рейтинга после лайка -1");

        reviewStorage.deleteReviewDislike(review1.getId(), user1.getId());
        useful = reviewStorage.findReviewById(newReview1.getId()).get().getUseful();

        assertEquals(0, useful, "Ожидаемое значение рейтинга после удаления лайка  0");
    }

    @Test
    public void testLikeDislike() {

        Film film = Film.builder()
                .name("film1")
                .releaseDate(LocalDate.of(1985, 10, 25))
                .duration(90L)
                .description("description")
                .mpa(MpaRating.builder().id(1).build())
                .build();
        film = filmStorage.saveFilm(film);
        User user1 = User.builder()
                .name("user1")
                .login("userLogin1")
                .email("test1@test.ru")
                .birthday(LocalDate.of(1980, 10, 25))
                .build();
        user1 = userStorage.saveUser(user1);

        Review review1 = Review.builder()
                .filmId(film.getId())
                .userId(user1.getId())
                .content("good film")
                .isPositive(true)
                .build();
        Review newReview1 = reviewStorage.saveReview(review1);

        reviewStorage.addReviewLike(review1.getId(), user1.getId());
        Integer useful = reviewStorage.findReviewById(newReview1.getId()).get().getUseful();

        assertEquals(1, useful, "Ожидаемое значение рейтинга после лайка 1");

        reviewStorage.addReviewDislike(review1.getId(), user1.getId());
        useful = reviewStorage.findReviewById(newReview1.getId()).get().getUseful();

        assertEquals(-1, useful, "Ожидаемое значение рейтинга после дизлайка  -1");
    }

}
