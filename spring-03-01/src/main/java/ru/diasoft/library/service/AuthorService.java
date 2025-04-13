package ru.diasoft.library.service;

import ru.diasoft.library.domain.Author;

public interface AuthorService {

    Author getById(long id);
    Author findByFullNameOrCreate(String fullName);
}
