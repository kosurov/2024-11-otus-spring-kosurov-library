package ru.diasoft.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.diasoft.library.dao.AuthorDao;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.exception.AuthorNotFoundException;
import ru.diasoft.library.mapper.AuthorMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Author> findAll() {
        return jdbcOperations.query("select * from authors", new AuthorMapper());
    }

    @Override
    public Author getById(long id) {
        try {
            return jdbcOperations.queryForObject("select * from authors where id = :id",
                    Map.of("id", id), new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorNotFoundException();
        }
    }

    @Override
    public Author save(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("fullname", author.getFullName());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into authors (id, fullname) values (nextval('authors_sequence_id'), :fullname)",
                params, keyHolder);
        author.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return author;
    }

    @Override
    public Author update(Author author) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", author.getId());
        params.put("fullname", author.getFullName());
        jdbcOperations.update("update authors set id = :id, fullname = :fullname where id = :id", params);
        return author;
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update("delete from authors where id = :id", Map.of("id", id));
    }

    @Override
    public Author getByFullName(String fullName) {
        try {
            return jdbcOperations.queryForObject("select * from authors where fullname = :fullname",
                    Map.of("fullname", fullName), new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorNotFoundException();
        }
    }
}
