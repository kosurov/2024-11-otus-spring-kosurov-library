package ru.diasoft.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diasoft.library.domain.UserProfile;
import ru.diasoft.library.domain.UserRole;
import ru.diasoft.library.dto.UserProfileDto;
import ru.diasoft.library.exception.InvalidCredentialsException;
import ru.diasoft.library.repository.UserProfileRepository;
import ru.diasoft.library.service.UserService;
import ru.diasoft.library.web.security.LibraryUserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileRepository.findById(username)
                .orElseThrow(InvalidCredentialsException::new);
        Set<String> roles = userProfile.getRoles().stream().map(UserRole::getName).collect(Collectors.toSet());
        return new LibraryUserDetails(userProfile.getUsername(), userProfile.getPassword(), roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> getAllUsers() {
        return userProfileRepository.findAll().stream()
                .map(u -> new UserProfileDto(u.getUsername()))
                .collect(Collectors.toList());
    }
}
