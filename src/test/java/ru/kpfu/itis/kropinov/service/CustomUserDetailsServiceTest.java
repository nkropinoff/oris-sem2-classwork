package ru.kpfu.itis.kropinov.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kpfu.itis.kropinov.model.Role;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_userFound_returnsCustomUserDetails() {
        User user = new User(1L, "user@mail.com");
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(List.of(role));

        given(userRepository.findByEmail("user@mail.com")).willReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("user@mail.com");

        assertNotNull(result);
        assertInstanceOf(CustomUserDetails.class, result);

        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertFalse(authorities.isEmpty());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_userWithNoRoles_returnsEmptyAuthorities() {
        User user = new User(1L, "user@mail.com");
        user.setRoles(List.of());

        given(userRepository.findByEmail("user@mail.com")).willReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("user@mail.com");

        assertTrue(result.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_userNotFound_throwsUsernameNotFoundException() {
        given(userRepository.findByEmail("unknown@mail.com")).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown@mail.com"));
    }
}