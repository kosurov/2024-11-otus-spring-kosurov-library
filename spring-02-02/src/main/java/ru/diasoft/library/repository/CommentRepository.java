package ru.diasoft.library.repository;

import ru.diasoft.library.domain.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> findAll();
    Comment getById(long id);
    Comment save(Comment comment);
    Comment update(Comment comment);
    void deleteById(long id);
}
