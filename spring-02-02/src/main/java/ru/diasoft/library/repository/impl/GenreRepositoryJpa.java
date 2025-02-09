package ru.diasoft.library.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.diasoft.library.repository.GenreRepository;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.GenreNotFoundException;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Genre getById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id)).orElseThrow(GenreNotFoundException::new);
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public Genre update(Genre genre) {
        Query query = em.createQuery("update Genre g set g.name = :name where g.id = :id");
        query.setParameter("name", genre.getName());
        query.setParameter("id", genre.getId());
        query.executeUpdate();
        return genre;
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public Genre getByName(String name) {
        try {
            TypedQuery<Genre> query =
                    em.createQuery("select g from Genre g where g.name = :name", Genre.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new GenreNotFoundException();
        }
    }
}
