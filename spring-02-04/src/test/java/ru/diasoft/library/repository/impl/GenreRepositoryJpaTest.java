package ru.diasoft.library.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.diasoft.library.domain.Genre;
import ru.diasoft.library.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами")
@DataJpaTest
class GenreRepositoryJpaTest {

    private static final int EXPECTED_QUANTITY_OF_GENRES = 2;
    private static final String NOT_EXISTED_GENRE_NAME = "Комедия";
    private static final long EXISTED_GENRE_ID = 1L;
    private static final String EXISTED_GENRE_NAME = "Детектив";

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращает все жанры")
    @Test
    void findAll_shouldReturnAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(EXPECTED_QUANTITY_OF_GENRES);
    }

    @DisplayName("Сохраняет жанр в БД")
    @Test
    void save_shouldSaveGenre() {
        Genre genre = new Genre(null, NOT_EXISTED_GENRE_NAME);
        List<Genre> genresBefore = genreRepository.findAll();
        genreRepository.save(genre);
        List<Genre> genresAfter = genreRepository.findAll();
        assertThat(genresAfter.size()).isEqualTo(genresBefore.size() + 1);
    }

    @DisplayName("Ищет жанр по name")
    @Test
    void getByName_shouldReturnGenre() {
        Genre expected = em.find(Genre.class, EXISTED_GENRE_ID);
        Optional<Genre> actual = genreRepository.findByName(EXISTED_GENRE_NAME);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(expected);
    }
}
