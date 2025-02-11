package ru.diasoft.library.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.diasoft.library.repository.BookRepository;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.dto.BookRequestDto;
import ru.diasoft.library.service.AuthorService;
import ru.diasoft.library.service.GenreService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с книгами")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorService authorService;
    @Mock
    private GenreService genreService;

    @DisplayName("Создает книгу")
    @Test
    void create_shouldCreateBook() {
        BookRequestDto bookDto = BookRequestDto.builder()
                .title("Книга")
                .authorFullName("Автор")
                .genre("Жанр")
                .build();
        Author author = new Author(1L, bookDto.getAuthorFullName());
        Genre genre = new Genre(2L, bookDto.getGenre());

        Book bookToSave = new Book();
        bookToSave.setAuthor(author);
        bookToSave.setGenre(genre);
        bookToSave.setTitle(bookDto.getTitle());

        Book savedBook = new Book();
        savedBook.setId(10L);
        savedBook.setAuthor(author);
        savedBook.setGenre(genre);
        savedBook.setTitle(bookDto.getTitle());

        when(authorService.findByFullNameOrCreate(bookDto.getAuthorFullName())).thenReturn(author);
        when(genreService.findByNameOrCreate(bookDto.getGenre())).thenReturn(genre);
        when(bookRepository.save(any())).thenReturn(savedBook);

        Book actual = bookService.create(bookDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(savedBook);
    }

    @DisplayName("Получает книгу по id")
    @Test
    void getById_shouldReturnBook() {
        long id = 10L;

        Author author = new Author(1L, "Автор");
        Genre genre = new Genre(2L, "Жанр");

        Book expected = new Book();
        expected.setId(10L);
        expected.setAuthor(author);
        expected.setGenre(genre);
        expected.setTitle("Книга");

        when(bookRepository.getById(id)).thenReturn(expected);

        Book actual = bookService.getById(id);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Получает все книги")
    @Test
    void findAll_shouldReturnAllBooks() {
        Author author = new Author(1L, "Автор");
        Genre genre = new Genre(2L, "Жанр");
        Book expected = new Book();
        expected.setId(10L);
        expected.setAuthor(author);
        expected.setGenre(genre);
        expected.setTitle("Книга");

        when(bookRepository.findAll()).thenReturn(Collections.singletonList(expected));

        List<Book> actual = bookService.findAll();

        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Обновляет книгу")
    @Test
    void update_shouldUpdateBook() {
        long id = 10L;
        BookRequestDto bookDto = BookRequestDto.builder()
                .id(id)
                .title("Книга Updated")
                .authorFullName("Автор Updated")
                .genre("Жанр Updated")
                .build();

        Author author = new Author(3L, bookDto.getAuthorFullName());
        Genre genre = new Genre(4L, bookDto.getGenre());

        Book bookFromDb = new Book();
        bookFromDb.setId(10L);
        bookFromDb.setAuthor(new Author(1L, null));
        bookFromDb.setGenre(new Genre(2L, null));
        bookFromDb.setTitle("Книга");

        Book updatedBook = new Book();
        updatedBook.setId(10L);
        updatedBook.setAuthor(author);
        updatedBook.setGenre(genre);
        updatedBook.setTitle(bookDto.getTitle());

        when(bookRepository.getById(id)).thenReturn(bookFromDb);
        when(authorService.findByFullNameOrCreate(bookDto.getAuthorFullName())).thenReturn(author);
        when(genreService.findByNameOrCreate(bookDto.getGenre())).thenReturn(genre);
        when(bookRepository.update(any())).thenReturn(updatedBook);

        Book actual = bookService.update(bookDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(updatedBook);
    }

    @DisplayName("Удаляет книгу")
    @Test
    void delete_shouldDeleteBook() {
        long id = 10L;
        bookService.delete(id);
        verify(bookRepository).deleteById(id);
    }
}
