package ru.kpfu.itis.kropinov.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kpfu.itis.kropinov.config.propreties.MailProperties;
import ru.kpfu.itis.kropinov.dto.UserCreateDto;
import ru.kpfu.itis.kropinov.dto.UserResponseDto;
import ru.kpfu.itis.kropinov.dto.UserUpdateDto;
import ru.kpfu.itis.kropinov.model.Role;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.repository.RoleRepository;
import ru.kpfu.itis.kropinov.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailProperties mailProperties;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_returnsListOfMappedDtos() {
        User user = new User(1L, "ivan@mail.com");
        given(userRepository.findAll()).willReturn(List.of(user));

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("ivan@mail.com", result.get(0).name());
    }

    @Test
    void getAllUsers_emptyRepository_returnsEmptyList() {
        given(userRepository.findAll()).willReturn(List.of());

        List<UserResponseDto> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getById_existingUser_returnsDto() {
        User user = new User(1L, "ivan@mail.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserResponseDto result = userService.getById(1L);

        assertEquals(1L, result.id());
        assertEquals("ivan@mail.com", result.name());
    }

    @Test
    void getById_userNotFound_throwsEntityNotFoundException() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getById(99L));
    }

    @Test
    void addUser_callsSaveOnRepository() {
        UserCreateDto dto = new UserCreateDto("newuser@mail.com");

        userService.addUser(dto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteById_existingUser_callsDeleteById() {
        given(userRepository.existsById(1L)).willReturn(true);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_userNotFound_throwsEntityNotFoundException() {
        given(userRepository.existsById(99L)).willReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteById(99L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void update_existingUser_returnsUpdatedDto() {
        UserUpdateDto dto = new UserUpdateDto(1L, "updated@mail.com");
        User savedUser = new User(1L, "updated@mail.com");
        given(userRepository.existsById(1L)).willReturn(true);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        UserResponseDto result = userService.update(dto);

        assertEquals("updated@mail.com", result.name());
    }

    @Test
    void update_userNotFound_throwsEntityNotFoundException() {
        UserUpdateDto dto = new UserUpdateDto(99L, "x@mail.com");
        given(userRepository.existsById(99L)).willReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.update(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_newEmail_savesUserAndSendsMail() throws Exception {
        Role role = new Role();
        role.setName("ROLE_USER");
        MimeMessage mimeMessage = new MimeMessage((jakarta.mail.Session) null);

        given(userRepository.findByEmail("new@mail.com")).willReturn(Optional.empty());
        given(passwordEncoder.encode("secret")).willReturn("encoded_secret");
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.of(role));
        given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);
        given(mailProperties.getContent()).willReturn("Hello $name, click $url");
        given(mailProperties.getFrom()).willReturn("noreply@app.com");
        given(mailProperties.getSender()).willReturn("MyApp");
        given(mailProperties.getSubject()).willReturn("Verify your email");
        given(mailProperties.getBaseUrl()).willReturn("http://localhost:8080");

        userService.register("new@mail.com", "secret");

        verify(userRepository, times(1)).save(any(User.class));
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void register_emailAlreadyExists_throwsRuntimeException() {
        given(userRepository.findByEmail("exists@mail.com"))
                .willReturn(Optional.of(new User("exists@mail.com")));

        assertThrows(RuntimeException.class,
                () -> userService.register("exists@mail.com", "pass"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_roleNotFound_throwsRuntimeException() {
        given(userRepository.findByEmail("a@b.com")).willReturn(Optional.empty());
        given(passwordEncoder.encode(any())).willReturn("encoded");
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.register("a@b.com", "pass"));
    }

    @Test
    void verifyUser_validCode_setsVerifiedAndReturnsTrue() {
        User user = new User("u@mail.com");
        given(userRepository.findByVerificationCode("valid-code"))
                .willReturn(Optional.of(user));

        boolean result = userService.verifyUser("valid-code");

        assertTrue(result);
        assertTrue(user.getVerified());
        verify(userRepository).save(user);
    }

    @Test
    void verifyUser_invalidCode_returnsFalse() {
        given(userRepository.findByVerificationCode("bad-code"))
                .willReturn(Optional.empty());

        boolean result = userService.verifyUser("bad-code");

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }
}