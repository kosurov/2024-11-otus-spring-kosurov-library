package ru.diasoft.library.service;

import ru.diasoft.library.dto.BookDto;

import java.util.List;

public interface BookService {

    BookDto create(BookDto bookDto);
    BookDto getById(long id);
    List<BookDto> findAll();
    BookDto update(long id, BookDto bookDto);
    void delete(long id);
}
