package ru.kpfu.itis.kropinov.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kropinov.dto.UserCreateDto;
import ru.kpfu.itis.kropinov.dto.UserResponseDto;
import ru.kpfu.itis.kropinov.dto.UserUpdateDto;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.repository.UserRepository;
import ru.kpfu.itis.kropinov.repository.UserRepositoryHibernate;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getName()))
                .toList();
    }

    public void addUser(UserCreateDto userCreateDto) {
        userRepository.save(new User(userCreateDto.name()));
    }

    public UserResponseDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return new UserResponseDto(user.getId(), user.getName());
    }

    public void deleteById(long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserResponseDto update(UserUpdateDto dto) {
        if (!userRepository.existsById(dto.id())) {
            throw new EntityNotFoundException("User not found: " + dto.id());
        }
        User updatedUser = userRepository.save(new User(dto.id(), dto.name()));
        return new UserResponseDto(updatedUser.getId(), updatedUser.getName());
    }
}
