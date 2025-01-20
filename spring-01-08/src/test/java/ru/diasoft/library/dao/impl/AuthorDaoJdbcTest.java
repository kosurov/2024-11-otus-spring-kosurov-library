package ru.diasoft.library.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.exception.AuthorNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с авторами")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    private static final int EXPECTED_QUANTITY_OF_AUTHORS = 3;
    private static final long NOT_EXISTED_AUTHOR_ID = 100L;
    private static final String NOT_EXISTED_AUTHOR_FULL_NAME = "Пушкин";
    private static final long EXISTED_AUTHOR_ID = 1L;
    private static final String EXISTED_AUTHOR_FULL_NAME = "Агата Кристи";

    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("Возвращает всех авторов")
    @Test
    void findAll_shouldReturnAllAuthors() {
        List<Author> authors = authorDao.findAll();
        assertThat(authors).hasSize(EXPECTED_QUANTITY_OF_AUTHORS);
    }

    @DisplayName("Ищет автора по id")
    @Test
    void getById_shouldReturnAuthor() {
        Author expected = new Author(EXISTED_AUTHOR_ID, EXISTED_AUTHOR_FULL_NAME);
        Author actual = authorDao.getById(expected.getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если автор не найден по id")
    @Test
    void getById_shouldThrowException() {
        assertThatThrownBy(() -> authorDao.getById(NOT_EXISTED_AUTHOR_ID))
                .isInstanceOf(AuthorNotFoundException.class);
    }

    @DisplayName("Сохраняет автора в БД")
    @Test
    void save_shouldSaveAuthor() {
        Author author = new Author(null, NOT_EXISTED_AUTHOR_FULL_NAME);
        List<Author> authorsBefore = authorDao.findAll();
        authorDao.save(author);
        List<Author> authorsAfter = authorDao.findAll();
        assertThat(authorsAfter.size()).isEqualTo(authorsBefore.size() + 1);
    }

    @DisplayName("Устанавливает id при сохранении автора в БД")
    @Test
    void save_shouldSetId() {
        Author author = new Author(null, NOT_EXISTED_AUTHOR_FULL_NAME);
        Author actual = authorDao.save(author);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Обновляет автора в БД")
    @Test
    void update_shouldUpdateAuthor() {
        Author existed = authorDao.getById(EXISTED_AUTHOR_ID);
        assertThat(existed.getFullName()).isNotEqualTo(NOT_EXISTED_AUTHOR_FULL_NAME);

        existed.setFullName(NOT_EXISTED_AUTHOR_FULL_NAME);
        authorDao.update(existed);
        Author actual = authorDao.getById(EXISTED_AUTHOR_ID);
        assertThat(actual.getFullName()).isEqualTo(NOT_EXISTED_AUTHOR_FULL_NAME);
    }

    @DisplayName("Удаляет автора из БД по id")
    @Test
    void deleteById_shouldDeleteAuthor() {
        assertThatNoException().isThrownBy(() -> authorDao.getById(EXISTED_AUTHOR_ID));
        authorDao.deleteById(EXISTED_AUTHOR_ID);
        assertThatThrownBy(() -> authorDao.getById(EXISTED_AUTHOR_ID))
                .isInstanceOf(AuthorNotFoundException.class);
    }

    @DisplayName("Ищет автора по fullName")
    @Test
    void getByFullName_shouldReturnAuthor() {
        Author expected = new Author(EXISTED_AUTHOR_ID, EXISTED_AUTHOR_FULL_NAME);
        Author actual = authorDao.getByFullName(EXISTED_AUTHOR_FULL_NAME);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если автор не найден по fullName")
    @Test
    void getByFullName_shouldThrowException() {
        assertThatThrownBy(() -> authorDao.getByFullName(NOT_EXISTED_AUTHOR_FULL_NAME))
                .isInstanceOf(AuthorNotFoundException.class);
    }
}
