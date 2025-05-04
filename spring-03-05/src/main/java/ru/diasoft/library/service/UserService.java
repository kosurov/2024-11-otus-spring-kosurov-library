package ru.diasoft.library.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.diasoft.library.dto.UserProfileDto;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserProfileDto> getAllUsers();
}
