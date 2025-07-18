package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.row.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.storage.api.DirectorStorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final DirectorRowMapper directorRowMapper;

    @Override
    public List<Director> findAll() {
        return jdbcTemplate.query("select * from directors", directorRowMapper);
    }

    @Override
    public Optional<Director> findDirectorById(Long id) {
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from directors where id = ?", directorRowMapper, id)
                )
        );
    }

    @Override
    public Director saveDirector(Director director) {
        String sql = "insert into directors (name) values (?)";

        GeneratedKeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, director.getName());
            return ps;
        }, kh);
        var generatedId = Optional.ofNullable(kh.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new RuntimeException("Id is not created"));
        director.setId(generatedId);
        log.info("Saved director: {}", director);
        return director;
    }

    @Override
    public Director updateDirector(Director director) {

        Director oldDirector = jdbcTemplate.queryForObject("select * from directors where id = ?", directorRowMapper, director.getId());
        if (oldDirector == null) {
            throw new NotFoundException("Director not found");
        }

        director.setName(director.getName());

        String sql = "UPDATE DIRECTORS SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());
        return director;
    }

    @Override
    public void removeDirector(Long id) {
        jdbcTemplate.update("delete from FILM_DIRECTOR where DIRECTOR_ID = ?", id);

        jdbcTemplate.update("delete from directors where id = ?", id);
    }

    @Override
    public Collection<Director> findDirectorsByParams(Collection<Long> params) {
        if (params.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "SELECT * FROM directors WHERE id IN (:ids) ORDER BY id ";

        SqlParameterSource parameters = new MapSqlParameterSource("ids", params);

        return namedParameterJdbcTemplate.query(sql, parameters, directorRowMapper);
    }

    @Override
    public List<Director> findDirectorsByDirectorId(Long directorId) {

        String sql = """
                SELECT D.*
                FROM FILM_DIRECTOR fd
                INNER JOIN DIRECTORS D on D.ID = fd.DIRECTOR_ID
                WHERE d.ID = ?
                """;
        return jdbcTemplate.query(sql, directorRowMapper, directorId);
    }

    @Override
    public List<Director> findDirectorsByFilmId(Long filmId) {
        String sql =
                """
                SELECT D.*
                FROM FILM_DIRECTOR fd
                INNER JOIN DIRECTORS D on D.ID = fd.DIRECTOR_ID
                WHERE fd.FILM_ID = ?
                """;
        return jdbcTemplate.query(sql, directorRowMapper, filmId);
    }
}
