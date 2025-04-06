package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diasoft.library.exception.BookNotFoundException;
import ru.diasoft.library.mapper.BookMapper;
import ru.diasoft.library.repository.BookRepository;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.service.AuthorService;
import ru.diasoft.library.service.BookService;
import ru.diasoft.library.service.GenreService;
import ru.diasoft.library.dto.BookDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService,
                           GenreService genreService, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional
    public BookDto create(BookDto bookDto) {
        Author author = authorService.findByFullNameOrCreate(bookDto.getAuthor());
        Genre genre = genreService.findByNameOrCreate(bookDto.getGenre());
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);
        return bookMapper.bookToBookDto(bookRepository.save(book));
    }

    @Override
    public BookDto getById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::bookToBookDto)
                .orElseThrow(BookNotFoundException::new);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::bookToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto update(long id, BookDto bookDto) {
        Author author = authorService.findByFullNameOrCreate(bookDto.getAuthor());
        Genre genre = genreService.findByNameOrCreate(bookDto.getGenre());
        Book existedBook = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        existedBook.setTitle(bookDto.getTitle());
        existedBook.setAuthor(author);
        existedBook.setGenre(genre);
        return bookMapper.bookToBookDto(bookRepository.save(existedBook));
    }

    @Override
    @Transactional
    public void delete(long id) {
        bookRepository.deleteById(id);
    }
}
