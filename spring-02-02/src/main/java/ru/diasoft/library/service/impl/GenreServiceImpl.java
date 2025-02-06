package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import ru.diasoft.library.dao.GenreDao;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.GenreNotFoundException;
import ru.diasoft.library.service.GenreService;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre getById(long id) {
        return genreDao.getById(id);
    }

    @Override
    public Genre findByNameOrCreate(String name) {
        try {
            return getByName(name);
        } catch (GenreNotFoundException e) {
            return create(name);
        }
    }

    private Genre getByName(String name) {
        return genreDao.getByName(name);
    }

    private Genre create(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        return genreDao.save(genre);
    }
}
