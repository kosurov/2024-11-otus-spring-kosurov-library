package ru.diasoft.library.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.exception.GenreNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с жанрами")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    private static final int EXPECTED_QUANTITY_OF_GENRES = 2;
    private static final long NOT_EXISTED_GENRE_ID = 100L;
    private static final String NOT_EXISTED_GENRE_NAME = "Комедия";
    private static final long EXISTED_GENRE_ID = 1L;
    private static final String EXISTED_GENRE_NAME = "Детектив";

    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("Возвращает все жанры")
    @Test
    void findAll_shouldReturnAllGenres() {
        List<Genre> genres = genreDao.findAll();
        assertThat(genres).hasSize(EXPECTED_QUANTITY_OF_GENRES);
    }

    @DisplayName("Ищет жанр по id")
    @Test
    void getById_shouldReturnGenre() {
        Genre expected = new Genre(EXISTED_GENRE_ID, EXISTED_GENRE_NAME);
        Genre actual = genreDao.getById(expected.getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если жанр не найден по id")
    @Test
    void getById_shouldThrowException() {
        assertThatThrownBy(() -> genreDao.getById(NOT_EXISTED_GENRE_ID))
                .isInstanceOf(GenreNotFoundException.class);
    }

    @DisplayName("Сохраняет жанр в БД")
    @Test
    void save_shouldSaveGenre() {
        Genre genre = new Genre(null, NOT_EXISTED_GENRE_NAME);
        List<Genre> genresBefore = genreDao.findAll();
        genreDao.save(genre);
        List<Genre> genresAfter = genreDao.findAll();
        assertThat(genresAfter.size()).isEqualTo(genresBefore.size() + 1);
    }

    @DisplayName("Устанавливает id при сохранении жанра в БД")
    @Test
    void save_shouldSetId() {
        Genre genre = new Genre(null, NOT_EXISTED_GENRE_NAME);
        Genre actual = genreDao.save(genre);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Обновляет жанр в БД")
    @Test
    void update_shouldUpdateGenre() {
        Genre existed = genreDao.getById(EXISTED_GENRE_ID);
        assertThat(existed.getName()).isNotEqualTo(NOT_EXISTED_GENRE_NAME);

        existed.setName(NOT_EXISTED_GENRE_NAME);
        genreDao.update(existed);
        Genre actual = genreDao.getById(EXISTED_GENRE_ID);
        assertThat(actual.getName()).isEqualTo(NOT_EXISTED_GENRE_NAME);
    }

    @DisplayName("Удаляет жанр из БД по id")
    @Test
    void deleteById_shouldDeleteGenre() {
        assertThatNoException().isThrownBy(() -> genreDao.getById(EXISTED_GENRE_ID));
        genreDao.deleteById(EXISTED_GENRE_ID);
        assertThatThrownBy(() -> genreDao.getById(EXISTED_GENRE_ID))
                .isInstanceOf(GenreNotFoundException.class);
    }

    @DisplayName("Ищет жанр по name")
    @Test
    void getByName_shouldReturnGenre() {
        Genre expected = new Genre(EXISTED_GENRE_ID, EXISTED_GENRE_NAME);
        Genre actual = genreDao.getByName(EXISTED_GENRE_NAME);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Выбрасывает исключение, если жанр не найден по name")
    @Test
    void getByName_shouldThrowException() {
        assertThatThrownBy(() -> genreDao.getByName(NOT_EXISTED_GENRE_NAME))
                .isInstanceOf(GenreNotFoundException.class);
    }

}
