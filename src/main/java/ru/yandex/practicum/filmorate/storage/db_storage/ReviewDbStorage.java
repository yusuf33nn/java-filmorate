package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.storage.api.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper;

    @Override
    public Review saveReview(Review review) {
        String sql = "INSERT INTO REVIEWS (CONTENT, ISPOSITIVE, USER_ID, FILM_ID) " +
                "VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, review.getContent());
            ps.setBoolean(2, review.getIsPositive());          // «login» здесь — обычная колонка
            ps.setLong(3, review.getUserId());
            ps.setLong(4, review.getFilmId());
            return ps;
        }, kh);
        var generatedId = Optional.ofNullable(kh.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new RuntimeException("Id is not created"));
        review.setId(generatedId);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sql = """
                UPDATE REVIEWS
                   SET CONTENT          = ?,
                       ISPOSITIVE   = ?,
                       USER_ID      = ?,
                       FILM_ID  = ?
                 WHERE id            = ?
                """;

        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getId());
        return review;
    }

    @Override
    public Optional<Review> findReviewById(Long reviewId) {
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from reviews where id = ?", reviewRowMapper, reviewId)
                )
        );
    }

    @Override
    public List<Review> findReviewByFilm(Long filmId, Long count) {
        String sql = "select * from reviews ";
        if (filmId >= 0)
            sql += " where film_id = " + filmId;
        sql += " order by useful  limit " + count;
        return jdbcTemplate.query(sql, reviewRowMapper);
    }

    @Override
    public void deleteReview(Long reviewId) {
        jdbcTemplate.update("DELETE FROM reviews WHERE id = ?", reviewId);
    }

    @Override
    public void addReviewLike(Long reviewId, Long userId) {
        String sql = "MERGE INTO reviews_grades (REVIEW_ID, USER_ID, grade) " +
                "VALUES (?, ?, 1)";

        jdbcTemplate.update(sql,
                reviewId,
                userId);
        calculateReviewGrade(reviewId);
    }

    @Override
    public void addReviewDislike(Long reviewId, Long userId) {
        String sql = "MERGE INTO reviews_grades (REVIEW_ID, USER_ID, GRADE) " +
                "VALUES (?, ?, -1)";

        jdbcTemplate.update(sql,
                reviewId,
                userId);
        calculateReviewGrade(reviewId);
    }

    @Override
    public void deleteReviewLike(Long reviewId, Long userId){
        String sql = "DELETE FROM reviews_grades WHERE REVIEW_ID = ? AND USER_ID = ? and GRADE = 1";
        jdbcTemplate.update(sql,
                reviewId,
                userId);
        calculateReviewGrade(reviewId);
    }

    @Override
    public void deleteReviewDislike(Long reviewId, Long userId) {
        String sql = "DELETE FROM reviews_grades WHERE REVIEW_ID = ? AND USER_ID = ? and GRADE = -1";
        jdbcTemplate.update(sql,
                reviewId,
                userId);
        calculateReviewGrade(reviewId);
    }


    void calculateReviewGrade(Long reviewId) {
        String sql = """
                UPDATE REVIEWS
                	SET useful	=   (SELECT sum(grade)
                						FROM reviews_grades
                					 WHERE review_id  = REVIEWS.id)
                 WHERE id            = ?
                """;

        jdbcTemplate.update(sql, reviewId);
    }


}
