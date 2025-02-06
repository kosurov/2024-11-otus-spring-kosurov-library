package ru.diasoft.library.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        long authorId = rs.getLong("authorid");
        long genreId = rs.getLong("genreid");
        Author author = new Author();
        author.setId(authorId);
        Genre genre = new Genre();
        genre.setId(genreId);
        return new Book(id, title, author, genre);
    }
}
