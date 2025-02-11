package ru.diasoft.library.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.exception.CommentNotFoundException;
import ru.diasoft.library.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> findAll() {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }

    @Override
    public Comment getById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id)).orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public Comment update(Comment comment) {
        Query query = em.createQuery(
                "update Comment c set c.text = :text where c.id = :id");
        query.setParameter("id", comment.getId());
        query.setParameter("text", comment.getText());
        query.executeUpdate();
        return comment;
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
