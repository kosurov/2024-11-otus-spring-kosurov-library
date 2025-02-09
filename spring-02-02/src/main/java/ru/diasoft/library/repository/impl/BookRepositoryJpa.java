package ru.diasoft.library.repository.impl;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.diasoft.library.repository.BookRepository;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.exception.BookNotFoundException;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("author-genre-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Book getById(long id) {
        return Optional.ofNullable(em.find(Book.class, id)).orElseThrow(BookNotFoundException::new);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Book update(Book book) {
        Query query = em.createQuery(
                "update Book b set b.title = :title, b.author = :author, b.genre = :genre where b.id = :id");
        query.setParameter("id", book.getId());
        query.setParameter("title", book.getTitle());
        query.setParameter("author", book.getAuthor());
        query.setParameter("genre", book.getGenre());
        query.executeUpdate();
        return book;
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
