package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.repository.BookRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами")
@DataJpaTest
class BookRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_BOOKS = 3;
    private static final String NOT_EXISTED_BOOK_TITLE = "Смерть лорда Эджвара";
    private static final long AUTHOR_ID = 1L;
    private static final long GENRE_ID = 1L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает все книги")
    @Test
    void findAll_shouldReturnAllBooks() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(EXPECTED_QUANTITY_OF_BOOKS);
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

        List<Book> booksBefore = bookRepository.findAll();
        bookRepository.save(book);
        List<Book> booksAfter = bookRepository.findAll();
        assertThat(booksAfter.size()).isEqualTo(booksBefore.size() + 1);
    }
}
