package ru.kpfu.itis.kropinov.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kropinov.dto.UserCreateDto;
import ru.kpfu.itis.kropinov.dto.UserResponseDto;
import ru.kpfu.itis.kropinov.dto.UserUpdateDto;
import ru.kpfu.itis.kropinov.model.Role;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.repository.RoleRepository;
import ru.kpfu.itis.kropinov.repository.UserRepository;
import ru.kpfu.itis.kropinov.repository.UserRepositoryHibernate;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername()))
                .toList();
    }

    public void addUser(UserCreateDto userCreateDto) {
        userRepository.save(new User(userCreateDto.name()));
    }

    public UserResponseDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return new UserResponseDto(user.getId(), user.getUsername());
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
        return new UserResponseDto(updatedUser.getId(), updatedUser.getUsername());
    }

    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists: " + username);
        }
        User user = new User(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(List.of(role));

        userRepository.save(user);
    }
}
