package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.BookNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Репозиторий для работы с книгами")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_BOOKS = 3;
    private static final long NOT_EXISTED_BOOK_ID = 100L;
    private static final long EXISTED_BOOK_ID = 1L;
    private static final String NOT_EXISTED_BOOK_TITLE = "Смерть лорда Эджвара";
    private static final long AUTHOR_ID = 1L;
    private static final long GENRE_ID = 1L;

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает все книги")
    @Test
    void findAll_shouldReturnAllBooks() {
        List<Book> books = bookRepositoryJpa.findAll();
        assertThat(books).hasSize(EXPECTED_QUANTITY_OF_BOOKS);
    }

    @DisplayName("Ищет книгу по id")
    @Test
    void getById_shouldReturnBook() {
        Book expected = em.find(Book.class, EXISTED_BOOK_ID);
        Book actual = bookRepositoryJpa.getById(EXISTED_BOOK_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если книга не найдена по id")
    @Test
    void getById_shouldThrowException() {
        assertThatThrownBy(() -> bookRepositoryJpa.getById(NOT_EXISTED_BOOK_ID))
                .isInstanceOf(BookNotFoundException.class);
    }

    @DisplayName("Сохраняет книгу в БД")
    @Test
    void save_shouldSaveBook() {
        Book book = new Book();
        Author author = em.find(Author.class, AUTHOR_ID);
        Genre genre =  em.find(Genre.class,GENRE_ID);
        book.setTitle(NOT_EXISTED_BOOK_TITLE);
        book.setAuthor(author);
        book.setGenre(genre);

        List<Book> booksBefore = bookRepositoryJpa.findAll();
        bookRepositoryJpa.save(book);
        List<Book> booksAfter = bookRepositoryJpa.findAll();
        assertThat(booksAfter.size()).isEqualTo(booksBefore.size() + 1);
    }

    @DisplayName("Устанавливает id при сохранении книги в БД")
    @Test
    void save_shouldSetId() {
        Book book = new Book();
        Author author = em.find(Author.class, AUTHOR_ID);
        Genre genre =  em.find(Genre.class,GENRE_ID);
        book.setTitle(NOT_EXISTED_BOOK_TITLE);
        book.setAuthor(author);
        book.setGenre(genre);

        Book actual = bookRepositoryJpa.save(book);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Обновляет книгу в БД")
    @Test
    void update_shouldUpdateBook() {
        Book existed = em.find(Book.class, EXISTED_BOOK_ID);
        Author author = existed.getAuthor();
        Genre genre = existed.getGenre();

        assertThat(existed.getTitle()).isNotEqualTo(NOT_EXISTED_BOOK_TITLE);
        em.detach(existed);

        Book toUpdate = new Book();
        toUpdate.setId(EXISTED_BOOK_ID);
        toUpdate.setTitle(NOT_EXISTED_BOOK_TITLE);
        toUpdate.setAuthor(author);
        toUpdate.setGenre(genre);

        bookRepositoryJpa.update(toUpdate);
        Book actual = em.find(Book.class, EXISTED_BOOK_ID);

        assertThat(actual.getTitle()).isEqualTo(NOT_EXISTED_BOOK_TITLE);
    }

    @DisplayName("Удаляет книгу из БД по id")
    @Test
    void deleteById_shouldDeleteBook() {
        Book book = em.find(Book.class, EXISTED_BOOK_ID);
        assertThat(book).isNotNull();
        em.detach(book);

        bookRepositoryJpa.deleteById(EXISTED_BOOK_ID);
        Book deleted = em.find(Book.class, EXISTED_BOOK_ID);
        assertThat(deleted).isNull();
    }

}
