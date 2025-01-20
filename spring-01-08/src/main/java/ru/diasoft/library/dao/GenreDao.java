package ru.diasoft.library.dao;

import ru.diasoft.library.domain.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findAll();
    Genre getById(long id);
    Genre save(Genre genre);
    Genre update(Genre genre);
    void deleteById(long id);
    Genre getByName(String name);
}
