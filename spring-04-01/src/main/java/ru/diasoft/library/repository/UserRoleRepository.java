package ru.diasoft.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diasoft.library.domain.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
