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
        String sql = "INSERT INTO PUBLIC.REVIEWS (CONTENT, ISPOSITIVE, USER_ID, FILM_ID) " +
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
        sql += " limit " + count;
        return jdbcTemplate.query(sql, reviewRowMapper);
    }
}
