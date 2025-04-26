package ru.diasoft.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diasoft.library.domain.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
