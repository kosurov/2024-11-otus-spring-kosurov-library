package ru.diasoft.library.repository.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.diasoft.library.repository.AuthorRepository;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.exception.AuthorNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Author getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id)).orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public Author update(Author author) {
        Query query = em.createQuery("update Author a set a.fullName = :fullName where a.id = :id");
        query.setParameter("fullName", author.getFullName());
        query.setParameter("id", author.getId());
        query.executeUpdate();
        return author;
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public Author getByFullName(String fullName) {
        try {
            TypedQuery<Author> query =
                    em.createQuery("select a from Author a where a.fullName = :fullName", Author.class);
            query.setParameter("fullName", fullName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new AuthorNotFoundException();
        }
    }
}
