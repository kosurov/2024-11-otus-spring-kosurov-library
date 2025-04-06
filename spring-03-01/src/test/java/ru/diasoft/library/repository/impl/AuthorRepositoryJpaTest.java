package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.diasoft.library.domain.Author;
import ru.diasoft.library.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами")
@DataJpaTest
class AuthorRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_AUTHORS = 3;
    private static final String NOT_EXISTED_AUTHOR_FULL_NAME = "Пушкин";
    private static final long EXISTED_AUTHOR_ID = 1L;
    private static final String EXISTED_AUTHOR_FULL_NAME = "Агата Кристи";

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает всех авторов")
    @Test
    void findAll_shouldReturnAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(EXPECTED_QUANTITY_OF_AUTHORS);
    }

    @DisplayName("Сохраняет автора в БД")
    @Test
    void save_shouldSaveAuthor() {
        Author author = new Author(null, NOT_EXISTED_AUTHOR_FULL_NAME);
        List<Author> authorsBefore = authorRepository.findAll();
        authorRepository.save(author);
        List<Author> authorsAfter = authorRepository.findAll();
        assertThat(authorsAfter.size()).isEqualTo(authorsBefore.size() + 1);
    }

    @DisplayName("Ищет автора по fullName")
    @Test
    void getByFullName_shouldReturnAuthor() {
        Author expected = em.find(Author.class, EXISTED_AUTHOR_ID);
        Optional<Author> actual = authorRepository.findByFullName(EXISTED_AUTHOR_FULL_NAME);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(expected);
    }
}
