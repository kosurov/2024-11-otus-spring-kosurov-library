package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diasoft.library.repository.GenreRepository;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.GenreNotFoundException;
import ru.diasoft.library.service.GenreService;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre getById(long id) {
        return genreRepository.findById(id).orElseThrow(GenreNotFoundException::new);
    }

    @Override
    @Transactional
    public Genre findByNameOrCreate(String name) {
        try {
            return getByName(name);
        } catch (GenreNotFoundException e) {
            return create(name);
        }
    }

    private Genre getByName(String name) {
        return genreRepository.findByName(name).orElseThrow(GenreNotFoundException::new);
    }

    private Genre create(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }
}
