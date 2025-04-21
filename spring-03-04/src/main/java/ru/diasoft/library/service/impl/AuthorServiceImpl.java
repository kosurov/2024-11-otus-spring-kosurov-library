package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diasoft.library.exception.AuthorNotFoundException;
import ru.diasoft.library.repository.AuthorRepository;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getById(long id) {
        return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    @Transactional
    public Author findByFullNameOrCreate(String fullName) {
        try {
            return getByFullName(fullName);
        } catch (AuthorNotFoundException e) {
            return create(fullName);
        }
    }

    private Author getByFullName(String name) {
        return authorRepository.findByFullName(name).orElseThrow(AuthorNotFoundException::new);
    }

    private Author create(String fullName) {
        Author author = new Author();
        author.setFullName(fullName);
        return authorRepository.save(author);
    }
}
