package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.repository.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями")
@DataJpaTest
public class CommentRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_COMMENTS = 3;
    private static final String NOT_EXISTED_COMMENT_TEXT = "Комментарий не сохранен";

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает все комментарии")
    @Test
    void findAll_shouldReturnAllComments() {
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(EXPECTED_QUANTITY_OF_COMMENTS);
    }

    @DisplayName("Сохраняет комментарий в БД")
    @Test
    void save_shouldSaveComment() {
        Book book = em.find(Book.class, 1L);
        Comment comment = new Comment(null, NOT_EXISTED_COMMENT_TEXT, book);
        List<Comment> commentsBefore = commentRepository.findAll();
        commentRepository.save(comment);
        List<Comment> commentsAfter = commentRepository.findAll();
        assertThat(commentsAfter.size()).isEqualTo(commentsBefore.size() + 1);
    }
}
