package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.exception.CommentNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Репозиторий для работы с комментариями")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
public class CommentRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_COMMENTS = 3;
    private static final long NOT_EXISTED_COMMENT_ID = 100L;
    private static final String NOT_EXISTED_COMMENT_TEXT = "Комментарий не сохранен";
    private static final long EXISTED_COMMENT_ID = 1L;

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает все комментарии")
    @Test
    void findAll_shouldReturnAllComments() {
        List<Comment> comments = commentRepositoryJpa.findAll();
        assertThat(comments).hasSize(EXPECTED_QUANTITY_OF_COMMENTS);
    }

    @DisplayName("Ищет комментарий по id")
    @Test
    void getById_shouldReturnComment() {
        Comment expected = em.find(Comment.class, EXISTED_COMMENT_ID);
        Comment actual = commentRepositoryJpa.getById(EXISTED_COMMENT_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если комментарий не найден по id")
    @Test
    void getById_shouldThrowException() {
        assertThatThrownBy(() -> commentRepositoryJpa.getById(NOT_EXISTED_COMMENT_ID))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @DisplayName("Сохраняет комментарий в БД")
    @Test
    void save_shouldSaveComment() {
        Comment comment = new Comment(null, NOT_EXISTED_COMMENT_TEXT);
        List<Comment> commentsBefore = commentRepositoryJpa.findAll();
        commentRepositoryJpa.save(comment);
        List<Comment> commentsAfter = commentRepositoryJpa.findAll();
        assertThat(commentsAfter.size()).isEqualTo(commentsBefore.size() + 1);
    }

    @DisplayName("Устанавливает id при сохранении комментария в БД")
    @Test
    void save_shouldSetId() {
        Comment comment = new Comment(null, NOT_EXISTED_COMMENT_TEXT);
        Comment actual = commentRepositoryJpa.save(comment);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Обновляет комментарий в БД")
    @Test
    void update_shouldUpdateComment() {
        Comment existed = em.find(Comment.class, EXISTED_COMMENT_ID);
        assertThat(existed.getText()).isNotEqualTo(NOT_EXISTED_COMMENT_TEXT);
        em.detach(existed);

        Comment toUpdate = new Comment(EXISTED_COMMENT_ID, NOT_EXISTED_COMMENT_TEXT);

        commentRepositoryJpa.update(toUpdate);

        Comment actual = em.find(Comment.class, EXISTED_COMMENT_ID);
        assertThat(actual.getText()).isEqualTo(NOT_EXISTED_COMMENT_TEXT);
    }

    @DisplayName("Удаляет комментарий из БД по id")
    @Test
    void deleteById_shouldDeleteComment() {
        Comment comment = em.find(Comment.class, EXISTED_COMMENT_ID);
        assertThat(comment).isNotNull();
        em.detach(comment);

        commentRepositoryJpa.deleteById(EXISTED_COMMENT_ID);
        Comment deleted = em.find(Comment.class, EXISTED_COMMENT_ID);
        assertThat(deleted).isNull();
    }
}
