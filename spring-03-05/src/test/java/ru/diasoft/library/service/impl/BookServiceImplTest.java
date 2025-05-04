package ru.diasoft.library.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.diasoft.library.mapper.BookMapper;
import ru.diasoft.library.mapper.BookMapperImpl;
import ru.diasoft.library.repository.BookRepository;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.service.AuthorService;
import ru.diasoft.library.service.GenreService;
import ru.diasoft.library.dto.BookDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @DisplayName("Создает книгу")
    @Test
    void create_shouldCreateBook() {
        BookDto bookDto = BookDto.builder()
                .title("Книга")
                .author("Автор")
                .genre("Жанр")
                .build();
        Author author = new Author(1L, bookDto.getAuthor());
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

        BookDto expected = new BookDto();
        expected.setId(10L);
        expected.setAuthor(author.getFullName());
        expected.setGenre(genre.getName());
        expected.setTitle("Книга");

        when(authorService.findByFullNameOrCreate(bookDto.getAuthor())).thenReturn(author);
        when(genreService.findByNameOrCreate(bookDto.getGenre())).thenReturn(genre);
        when(bookRepository.save(any())).thenReturn(savedBook);

        BookDto actual = bookService.create(bookDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Получает книгу по id")
    @Test
    void getById_shouldReturnBook() {
        long id = 10L;

        Author author = new Author(1L, "Автор");
        Genre genre = new Genre(2L, "Жанр");

        Book book = new Book();
        book.setId(10L);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setTitle("Книга");

        BookDto expected = new BookDto();
        expected.setId(10L);
        expected.setAuthor(author.getFullName());
        expected.setGenre(genre.getName());
        expected.setTitle("Книга");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        BookDto actual = bookService.getById(id);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Получает все книги")
    @Test
    void findAll_shouldReturnAllBooks() {
        Author author = new Author(1L, "Автор");
        Genre genre = new Genre(2L, "Жанр");
        Book book = new Book();
        book.setId(10L);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setTitle("Книга");

        BookDto expected = new BookDto();
        expected.setId(10L);
        expected.setAuthor(author.getFullName());
        expected.setGenre(genre.getName());
        expected.setTitle("Книга");

        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));

        List<BookDto> actual = bookService.findAll();

        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Обновляет книгу")
    @Test
    void update_shouldUpdateBook() {
        long id = 10L;
        BookDto bookDto = BookDto.builder()
                .id(id)
                .title("Книга Updated")
                .author("Автор Updated")
                .genre("Жанр Updated")
                .build();

        Author author = new Author(3L, bookDto.getAuthor());
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

        BookDto expected = new BookDto();
        expected.setId(10L);
        expected.setAuthor(author.getFullName());
        expected.setGenre(genre.getName());
        expected.setTitle(bookDto.getTitle());

        when(bookRepository.findById(id)).thenReturn(Optional.of(bookFromDb));
        when(authorService.findByFullNameOrCreate(bookDto.getAuthor())).thenReturn(author);
        when(genreService.findByNameOrCreate(bookDto.getGenre())).thenReturn(genre);
        when(bookRepository.save(any())).thenReturn(updatedBook);

        BookDto actual = bookService.update(bookDto.getId(), bookDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Удаляет книгу")
    @Test
    void delete_shouldDeleteBook() {
        long id = 10L;
        bookService.delete(id);
        verify(bookRepository).deleteById(id);
    }
}
