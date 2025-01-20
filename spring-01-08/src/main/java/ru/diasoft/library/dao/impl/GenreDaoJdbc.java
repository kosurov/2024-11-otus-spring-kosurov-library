package ru.diasoft.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.diasoft.library.dao.GenreDao;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.GenreNotFoundException;
import ru.diasoft.library.mapper.GenreMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Genre> findAll() {
        return jdbcOperations.query("select * from genres", new GenreMapper());
    }

    @Override
    public Genre getById(long id) {
        try {
            return jdbcOperations.queryForObject("select * from genres where id = :id",
                    Map.of("id", id), new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException();
        }
    }

    @Override
    public Genre save(Genre genre) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into genres (id, name) values (nextval('genres_sequence_id'), :name)",
                params, keyHolder);
        genre.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        jdbcOperations.update("update genres set id = :id, name = :name where id = :id", params);
        return genre;
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update("delete from genres where id = :id",  Map.of("id", id));
    }

    @Override
    public Genre getByName(String name) {
        try {
            return jdbcOperations.queryForObject("select * from genres where name = :name",
                    Map.of("name", name), new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException();
        }
    }
}
