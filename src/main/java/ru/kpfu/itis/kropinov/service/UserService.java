package ru.kpfu.itis.kropinov.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getEmail()))
                .toList();
    }

    public void addUser(UserCreateDto userCreateDto) {
        userRepository.save(new User(userCreateDto.name()));
    }

    public UserResponseDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return new UserResponseDto(user.getId(), user.getEmail());
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
        return new UserResponseDto(updatedUser.getId(), updatedUser.getEmail());
    }

    public void register(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists: " + email  );
        }
        User user = new User(email);
        user.setPassword(passwordEncoder.encode(password));

        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);
        user.setVerified(false);

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(List.of(role));
        userRepository.save(user);

        sendVerificationMail(email, verificationCode);
    }

    @Transactional
    public boolean verifyUser(String verificationCode) {
        Optional<User> userOptional = userRepository.findByVerificationCode(verificationCode);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private void sendVerificationMail(String email, String verificationCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String content = mailProperties.getContent();
            mimeMessageHelper.setFrom(mailProperties.getFrom(), mailProperties.getSender());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(mailProperties.getSubject());

            String formattedContent = content.replace("$name", email).replace("$url", mailProperties.getBaseUrl() + "/verification?code=" + verificationCode);
            mimeMessageHelper.setText(formattedContent, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            // ...
            throw new RuntimeException(e);
        }
    }
}
