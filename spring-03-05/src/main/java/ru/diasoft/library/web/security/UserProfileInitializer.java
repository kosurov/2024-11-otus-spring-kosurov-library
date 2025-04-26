package ru.diasoft.library.web.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.diasoft.library.domain.UserProfile;
import ru.diasoft.library.repository.UserProfileRepository;

@Component
@RequiredArgsConstructor
public class UserProfileInitializer {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        userProfileRepository.save(new UserProfile("user", passwordEncoder.encode("querty")));
        userProfileRepository.save(new UserProfile("admin", passwordEncoder.encode("admin123")));
    }
}
