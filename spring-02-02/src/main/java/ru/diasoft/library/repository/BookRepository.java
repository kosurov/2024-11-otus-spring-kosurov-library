package ru.diasoft.library.repository;

import ru.diasoft.library.domain.Book;

import java.util.List;

public interface BookRepository {

    List<Book> findAll();
    Book getById(long id);
    Book save(Book book);
    Book update(Book book);
    void deleteById(long id);
}
