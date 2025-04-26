package ru.diasoft.library.web.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.diasoft.library.domain.UserProfile;
import ru.diasoft.library.domain.UserRole;
import ru.diasoft.library.repository.UserProfileRepository;
import ru.diasoft.library.repository.UserRoleRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserProfileInitializer {

    private final UserProfileRepository userProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        UserRole user = userRoleRepository.save(new UserRole(null, "ROLE_USER"));
        UserRole admin = userRoleRepository.save(new UserRole(null, "ROLE_ADMIN"));
        UserRole moderator = userRoleRepository.save(new UserRole(null, "ROLE_MODERATOR"));

        userProfileRepository.save(new UserProfile("user", passwordEncoder.encode("qwerty"), Set.of(user)));
        userProfileRepository.save(new UserProfile("admin", passwordEncoder.encode("admin123"), Set.of(admin)));
        userProfileRepository.save(new UserProfile("moderator", passwordEncoder.encode("moderator456"), Set.of(admin, moderator)));
    }
}
