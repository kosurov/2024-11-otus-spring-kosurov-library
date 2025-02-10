package ru.diasoft.library.service;

import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findAllByBookId(long bookId);
    Comment create(CommentDto commentDto);
    void delete(long id);
}
