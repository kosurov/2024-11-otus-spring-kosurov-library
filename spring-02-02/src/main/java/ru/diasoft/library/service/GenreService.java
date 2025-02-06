package ru.diasoft.library.service;

import ru.diasoft.library.domain.Genre;

public interface GenreService {

    Genre getById(long id);
    Genre findByNameOrCreate(String name);
}
