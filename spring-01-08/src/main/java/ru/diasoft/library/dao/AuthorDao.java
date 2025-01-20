package ru.diasoft.library.dao;

import ru.diasoft.library.domain.Author;

import java.util.List;

public interface AuthorDao {

    List<Author> findAll();
    Author getById(long id);
    Author save(Author author);
    Author update(Author author);
    void deleteById(long id);
    Author getByFullName(String fullName);
}
