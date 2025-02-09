package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import ru.diasoft.library.repository.BookRepository;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.dto.BookRequestDto;
import ru.diasoft.library.service.AuthorService;
import ru.diasoft.library.service.BookService;
import ru.diasoft.library.service.GenreService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, GenreService genreService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public Book create(BookRequestDto bookDto) {
        Author author = authorService.findByFullNameOrCreate(bookDto.getAuthorFullName());
        Genre genre = genreService.findByNameOrCreate(bookDto.getGenre());
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    @Override
    public Book getById(long id) {
        return setBookRelations(bookRepository.getById(id));
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            setBookRelations(book);
        }
        return books;
    }

    @Override
    public Book update(BookRequestDto bookDto) {
        Author author = authorService.findByFullNameOrCreate(bookDto.getAuthorFullName());
        Genre genre = genreService.findByNameOrCreate(bookDto.getGenre());
        Book existedBook = bookRepository.getById(bookDto.getId());
        existedBook.setTitle(bookDto.getTitle());
        existedBook.setAuthor(author);
        existedBook.setGenre(genre);
        return bookRepository.update(existedBook);
    }

    @Override
    public void delete(long id) {
        bookRepository.deleteById(id);
    }

    private Book setBookRelations(Book book) {
        book.setAuthor(authorService.getById(book.getAuthor().getId()));
        book.setGenre(genreService.getById(book.getGenre().getId()));
        return book;
    }
}
