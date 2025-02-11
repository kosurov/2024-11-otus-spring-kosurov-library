package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.dto.CommentDto;
import ru.diasoft.library.repository.BookRepository;
import ru.diasoft.library.repository.CommentRepository;
import ru.diasoft.library.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public List<CommentDto> findAllByBookId(long bookId) {
        Book book = bookRepository.getById(bookId);
        return book.getComments().stream()
                .map(c -> getCommentDto(bookId, c))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Comment create(CommentDto commentDto) {
        Book book = bookRepository.getById(commentDto.getBookId());
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        Comment savedComment = commentRepository.save(comment);
        book.getComments().add(comment);
        return savedComment;
    }

    @Override
    @Transactional
    public void delete(long id) {
        commentRepository.deleteById(id);
    }

    private CommentDto getCommentDto(long bookId, Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .bookId(bookId)
                .text(comment.getText()).build();
    }
}
