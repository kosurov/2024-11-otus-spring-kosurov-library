package ru.diasoft.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diasoft.library.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByFullName(String fullName);
}
