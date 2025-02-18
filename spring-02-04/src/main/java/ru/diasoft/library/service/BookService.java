package ru.diasoft.library.service;

import ru.diasoft.library.domain.Book;
import ru.diasoft.library.dto.BookRequestDto;

import java.util.List;

public interface BookService {

    Book create(BookRequestDto bookDto);
    Book getById(long id);
    List<Book> findAll();
    Book update(BookRequestDto bookDto);
    void delete(long id);
}
