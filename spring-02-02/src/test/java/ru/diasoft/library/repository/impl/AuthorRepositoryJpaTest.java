package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.exception.AuthorNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Репозиторий для работы с авторами")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_AUTHORS = 3;
    private static final long NOT_EXISTED_AUTHOR_ID = 100L;
    private static final String NOT_EXISTED_AUTHOR_FULL_NAME = "Пушкин";
    private static final long EXISTED_AUTHOR_ID = 1L;
    private static final String EXISTED_AUTHOR_FULL_NAME = "Агата Кристи";

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает всех авторов")
    @Test
    void findAll_shouldReturnAllAuthors() {
        List<Author> authors = authorRepositoryJpa.findAll();
        assertThat(authors).hasSize(EXPECTED_QUANTITY_OF_AUTHORS);
    }

    @DisplayName("Ищет автора по id")
    @Test
    void getById_shouldReturnAuthor() {
        Author expected = em.find(Author.class, EXISTED_AUTHOR_ID);
        Author actual = authorRepositoryJpa.getById(expected.getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если автор не найден по id")
    @Test
    void getById_shouldThrowException() {
        assertThatThrownBy(() -> authorRepositoryJpa.getById(NOT_EXISTED_AUTHOR_ID))
                .isInstanceOf(AuthorNotFoundException.class);
    }

    @DisplayName("Сохраняет автора в БД")
    @Test
    void save_shouldSaveAuthor() {
        Author author = new Author(null, NOT_EXISTED_AUTHOR_FULL_NAME);
        List<Author> authorsBefore = authorRepositoryJpa.findAll();
        authorRepositoryJpa.save(author);
        List<Author> authorsAfter = authorRepositoryJpa.findAll();
        assertThat(authorsAfter.size()).isEqualTo(authorsBefore.size() + 1);
    }

    @DisplayName("Устанавливает id при сохранении автора в БД")
    @Test
    void save_shouldSetId() {
        Author author = new Author(null, NOT_EXISTED_AUTHOR_FULL_NAME);
        Author actual = authorRepositoryJpa.save(author);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Обновляет автора в БД")
    @Test
    void update_shouldUpdateAuthor() {
        Author existed = em.find(Author.class, EXISTED_AUTHOR_ID);
        assertThat(existed.getFullName()).isNotEqualTo(NOT_EXISTED_AUTHOR_FULL_NAME);
        em.detach(existed);

        Author toUpdate = new Author(EXISTED_AUTHOR_ID, NOT_EXISTED_AUTHOR_FULL_NAME);

        authorRepositoryJpa.update(toUpdate);

        Author actual = em.find(Author.class, EXISTED_AUTHOR_ID);
        assertThat(actual.getFullName()).isEqualTo(NOT_EXISTED_AUTHOR_FULL_NAME);
    }

    @DisplayName("Удаляет автора из БД по id")
    @Test
    void deleteById_shouldDeleteAuthor() {
        Author author = em.find(Author.class, EXISTED_AUTHOR_ID);
        assertThat(author).isNotNull();
        em.detach(author);

        authorRepositoryJpa.deleteById(EXISTED_AUTHOR_ID);
        Author deleted = em.find(Author.class, EXISTED_AUTHOR_ID);
        assertThat(deleted).isNull();
    }

    @DisplayName("Ищет автора по fullName")
    @Test
    void getByFullName_shouldReturnAuthor() {
        Author expected = em.find(Author.class, EXISTED_AUTHOR_ID);
        Author actual = authorRepositoryJpa.getByFullName(EXISTED_AUTHOR_FULL_NAME);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если автор не найден по fullName")
    @Test
    void getByFullName_shouldThrowException() {
        assertThatThrownBy(() -> authorRepositoryJpa.getByFullName(NOT_EXISTED_AUTHOR_FULL_NAME))
                .isInstanceOf(AuthorNotFoundException.class);
    }
}
