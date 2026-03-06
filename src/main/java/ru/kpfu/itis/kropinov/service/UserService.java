package ru.kpfu.itis.kropinov.service;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.kropinov.dto.UserCreateDto;
import ru.kpfu.itis.kropinov.dto.UserResponseDto;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserResponseDto(user.getId(), user.getName())).toList();
    }

    public void addUser(UserCreateDto userCreateDto) {
        userRepository.save(new User(userCreateDto.name()));
    }

}
