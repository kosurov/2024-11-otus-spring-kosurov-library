package ru.diasoft.library.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.diasoft.library.domain.UserProfile;
import ru.diasoft.library.exception.InvalidCredentialsException;
import ru.diasoft.library.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileRepository.findById(username)
                .orElseThrow(InvalidCredentialsException::new);
        return new LibraryUserDetails(userProfile.getUsername(), userProfile.getPassword());
    }
}
