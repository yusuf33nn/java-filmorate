package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;
import ru.yandex.practicum.filmorate.storage.api.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingRowMapper mpaRatingRowMapper;

    @Override
    public List<MpaRating> getAllMpaRatings() {
        return jdbcTemplate.query("SELECT * FROM mpa_rating", mpaRatingRowMapper);
    }

    @Override
    public Optional<MpaRating> getMpaRatingById(int id) {
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("SELECT * FROM mpa_rating WHERE id = ?", mpaRatingRowMapper, id)
                ));
    }
}
