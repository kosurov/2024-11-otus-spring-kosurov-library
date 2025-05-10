package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.GenreNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Репозиторий для работы с жанрами")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_GENRES = 2;
    private static final long NOT_EXISTED_GENRE_ID = 100L;
    private static final String NOT_EXISTED_GENRE_NAME = "Комедия";
    private static final long EXISTED_GENRE_ID = 1L;
    private static final String EXISTED_GENRE_NAME = "Детектив";

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает все жанры")
    @Test
    void findAll_shouldReturnAllGenres() {
        List<Genre> genres = genreRepositoryJpa.findAll();
        assertThat(genres).hasSize(EXPECTED_QUANTITY_OF_GENRES);
    }

    @DisplayName("Ищет жанр по id")
    @Test
    void getById_shouldReturnGenre() {
        Genre expected = em.find(Genre.class, EXISTED_GENRE_ID);
        Genre actual = genreRepositoryJpa.getById(expected.getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если жанр не найден по id")
    @Test
    void getById_shouldThrowException() {
        assertThatThrownBy(() -> genreRepositoryJpa.getById(NOT_EXISTED_GENRE_ID))
                .isInstanceOf(GenreNotFoundException.class);
    }

    @DisplayName("Сохраняет жанр в БД")
    @Test
    void save_shouldSaveGenre() {
        Genre genre = new Genre(null, NOT_EXISTED_GENRE_NAME);
        List<Genre> genresBefore = genreRepositoryJpa.findAll();
        genreRepositoryJpa.save(genre);
        List<Genre> genresAfter = genreRepositoryJpa.findAll();
        assertThat(genresAfter.size()).isEqualTo(genresBefore.size() + 1);
    }

    @DisplayName("Устанавливает id при сохранении жанра в БД")
    @Test
    void save_shouldSetId() {
        Genre genre = new Genre(null, NOT_EXISTED_GENRE_NAME);
        Genre actual = genreRepositoryJpa.save(genre);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Обновляет жанр в БД")
    @Test
    void update_shouldUpdateGenre() {
        Genre existed = em.find(Genre.class, EXISTED_GENRE_ID);
        assertThat(existed.getName()).isNotEqualTo(NOT_EXISTED_GENRE_NAME);
        em.detach(existed);

        Genre toUpdate = new Genre(EXISTED_GENRE_ID, NOT_EXISTED_GENRE_NAME);

        genreRepositoryJpa.update(toUpdate);
        Genre actual = em.find(Genre.class, EXISTED_GENRE_ID);
        assertThat(actual.getName()).isEqualTo(NOT_EXISTED_GENRE_NAME);
    }

    @DisplayName("Удаляет жанр из БД по id")
    @Test
    void deleteById_shouldDeleteGenre() {
        Genre genre = em.find(Genre.class, EXISTED_GENRE_ID);
        assertThat(genre).isNotNull();
        em.detach(genre);

        genreRepositoryJpa.deleteById(EXISTED_GENRE_ID);
        Genre deleted = em.find(Genre.class, EXISTED_GENRE_ID);
        assertThat(deleted).isNull();
    }

    @DisplayName("Ищет жанр по name")
    @Test
    void getByName_shouldReturnGenre() {
        Genre expected = em.find(Genre.class, EXISTED_GENRE_ID);
        Genre actual = genreRepositoryJpa.getByName(EXISTED_GENRE_NAME);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если жанр не найден по name")
    @Test
    void getByName_shouldThrowException() {
        assertThatThrownBy(() -> genreRepositoryJpa.getByName(NOT_EXISTED_GENRE_NAME))
                .isInstanceOf(GenreNotFoundException.class);
    }

}
