package ru.diasoft.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.dto.CommentDto;
import ru.diasoft.library.exception.BookNotFoundException;
import ru.diasoft.library.mapper.CommentMapper;
import ru.diasoft.library.repository.BookRepository;
import ru.diasoft.library.repository.CommentRepository;
import ru.diasoft.library.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    @Transactional
    public List<CommentDto> findAllByBookId(long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        return book.getComments().stream()
                .map(c -> getCommentDto(bookId, c))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto commentDto) {
        Book book = bookRepository.findById(commentDto.getBookId()).orElseThrow(BookNotFoundException::new);
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setBook(book);
        return commentMapper.commentToCommentDto(commentRepository.save(comment));
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
