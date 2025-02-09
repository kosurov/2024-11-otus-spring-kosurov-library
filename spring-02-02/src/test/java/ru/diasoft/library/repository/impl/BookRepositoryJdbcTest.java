package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.BookNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с книгами")
@JdbcTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJdbcTest {

    private static final int EXPECTED_QUANTITY_OF_BOOKS = 3;
    private static final long NOT_EXISTED_BOOK_ID = 100L;
    private static final long EXISTED_BOOK_ID = 1L;
    private static final String EXISTED_BOOK_TITLE = "Убийство в восточном экспрессе";
    private static final String NOT_EXISTED_BOOK_TITLE = "Смерть лорда Эджвара";
    private static final long AUTHOR_ID = 1L;
    private static final long GENRE_ID = 1L;

    @Autowired
    private BookRepositoryJpa bookDao;

    @DisplayName("Возвращает все книги")
    @Test
    void findAll_shouldReturnAllBooks() {
        List<Book> books = bookDao.findAll();
        assertThat(books).hasSize(EXPECTED_QUANTITY_OF_BOOKS);
    }

    @DisplayName("Ищет книгу по id")
    @Test
    void getById_shouldReturnBook() {
        Book expected = new Book();
        Author author = new Author(AUTHOR_ID, null);
        Genre genre = new Genre(GENRE_ID, null);
        expected.setId(EXISTED_BOOK_ID);
        expected.setTitle(EXISTED_BOOK_TITLE);
        expected.setAuthor(author);
        expected.setGenre(genre);

        Book actual = bookDao.getById(expected.getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если книга не найдена по id")
    @Test
    void getById_shouldThrowException() {
        assertThatThrownBy(() -> bookDao.getById(NOT_EXISTED_BOOK_ID))
                .isInstanceOf(BookNotFoundException.class);
    }

    @DisplayName("Сохраняет книгу в БД")
    @Test
    void save_shouldSaveBook() {
        Book book = new Book();
        Author author = new Author(AUTHOR_ID, null);
        Genre genre = new Genre(GENRE_ID, null);
        book.setTitle(NOT_EXISTED_BOOK_TITLE);
        book.setAuthor(author);
        book.setGenre(genre);

        List<Book> booksBefore = bookDao.findAll();
        bookDao.save(book);
        List<Book> booksAfter = bookDao.findAll();
        assertThat(booksAfter.size()).isEqualTo(booksBefore.size() + 1);
    }

    @DisplayName("Устанавливает id при сохранении книги в БД")
    @Test
    void save_shouldSetId() {
        Book book = new Book();
        Author author = new Author(AUTHOR_ID, null);
        Genre genre = new Genre(GENRE_ID, null);
        book.setTitle(NOT_EXISTED_BOOK_TITLE);
        book.setAuthor(author);
        book.setGenre(genre);

        Book actual = bookDao.save(book);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Обновляет книгу в БД")
    @Test
    void update_shouldUpdateBook() {
        Book existed = bookDao.getById(EXISTED_BOOK_ID);
        assertThat(existed.getTitle()).isNotEqualTo(NOT_EXISTED_BOOK_TITLE);

        existed.setTitle(NOT_EXISTED_BOOK_TITLE);
        bookDao.update(existed);
        Book actual = bookDao.getById(EXISTED_BOOK_ID);
        assertThat(actual.getTitle()).isEqualTo(NOT_EXISTED_BOOK_TITLE);
    }

    @DisplayName("Удаляет книгу из БД по id")
    @Test
    void deleteById_shouldDeleteBook() {
        assertThatNoException().isThrownBy(() -> bookDao.getById(EXISTED_BOOK_ID));
        bookDao.deleteById(EXISTED_BOOK_ID);
        assertThatThrownBy(() -> bookDao.getById(EXISTED_BOOK_ID))
                .isInstanceOf(BookNotFoundException.class);
    }

}
