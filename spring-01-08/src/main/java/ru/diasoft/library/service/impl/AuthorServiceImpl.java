package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import ru.diasoft.library.dao.AuthorDao;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.exception.AuthorNotFoundException;
import ru.diasoft.library.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }
    @Override
    public Author getById(long id) {
        return authorDao.getById(id);
    }

    @Override
    public Author findByFullNameOrCreate(String fullName) {
        try {
            return getByFullName(fullName);
        } catch (AuthorNotFoundException e) {
            return create(fullName);
        }
    }

    private Author getByFullName(String name) {
        return authorDao.getByFullName(name);
    }

    private Author create(String fullName) {
        Author author = new Author();
        author.setFullName(fullName);
        return authorDao.save(author);
    }
}
