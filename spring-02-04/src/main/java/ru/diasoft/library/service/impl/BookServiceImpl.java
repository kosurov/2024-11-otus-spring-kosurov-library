package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diasoft.library.exception.BookNotFoundException;
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
    @Transactional
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
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book update(BookRequestDto bookDto) {
        Author author = authorService.findByFullNameOrCreate(bookDto.getAuthorFullName());
        Genre genre = genreService.findByNameOrCreate(bookDto.getGenre());
        Book existedBook = bookRepository.findById(bookDto.getId()).orElseThrow(BookNotFoundException::new);
        existedBook.setTitle(bookDto.getTitle());
        existedBook.setAuthor(author);
        existedBook.setGenre(genre);
        return bookRepository.save(existedBook);
    }

    @Override
    @Transactional
    public void delete(long id) {
        bookRepository.deleteById(id);
    }
}
