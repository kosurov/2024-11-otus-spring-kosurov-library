package ru.diasoft.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.diasoft.library.dao.BookDao;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.exception.BookNotFoundException;
import ru.diasoft.library.mapper.BookMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Book> findAll() {
        return jdbcOperations.query("select * from books", new BookMapper());
    }

    @Override
    public Book getById(long id) {
        try {
            return jdbcOperations.queryForObject("select * from books where id = :id",
                    Map.of("id", id), new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundException();
        }
    }

    @Override
    public Book save(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorid", book.getAuthor().getId());
        params.addValue("genreid", book.getGenre().getId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into books (id, title, authorid, genreid) values (nextval('books_sequence_id'), :title, :authorid, :genreid)",
                params, keyHolder);
        book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return book;
    }

    @Override
    public Book update(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        params.put("authorid", book.getAuthor().getId());
        params.put("genreid", book.getGenre().getId());
        jdbcOperations.update("update books set id = :id, title = :title, authorid = :authorid, genreid = :genreid where id = :id",
                params);
        return book;
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcOperations.update("delete from books where id = :id", Map.of("id", id));
    }
}
