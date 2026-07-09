package com.pen.taskmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;
import com.pen.taskmanagement.exceptions.ForbiddenException;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.UserMapper;
import com.pen.taskmanagement.model.RoleEnum;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.UserRepository;
import com.pen.taskmanagement.service.UserServiceImpl;
import com.pen.taskmanagement.utilities.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    SecurityUtil securityUtil;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void shouldCreate() {

        UserRequest request = new UserRequest("Sergio", "Perez", "sergio@example.com", "tornado", "password123");
        User user = new User();
        user.setUsername("tornado");
        UserResponse userResponse = new UserResponse(null, null, null);

        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userServiceImpl.createUser(request);

        assertEquals(userResponse, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateSuccessfullyAsSelf() {

        User user = new User();
        user.setId(1L);
        user.setUsername("tornado");
        user.setRole(RoleEnum.USER);

        UserRequest request = new UserRequest("Sergio", "Perez", "sergio@example.com", "tornado", "newPassword");
        UserResponse userResponse = new UserResponse(null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityUtil.extractUsername()).thenReturn("tornado");
        when(passwordEncoder.encode("newPassword")).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userServiceImpl.updateUser(request, 1L);

        assertEquals(userResponse, result);
    }

    @Test
    void shouldUpdateSuccessfullyAsAdmin() {

        User user = new User();
        user.setId(1L);
        user.setUsername("someoneElse");

        UserRequest request = new UserRequest("Sergio", "Perez", "sergio@example.com", "someoneElse", "newPassword");
        UserResponse userResponse = new UserResponse(null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityUtil.extractUsername()).thenReturn("admin");
        when(securityUtil.isAdmin()).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userServiceImpl.updateUser(request, 1L);

        assertEquals(userResponse, result);
    }

    @Test
    void shouldThrowForbiddenWhenUpdatingWithWrongUser() {

        User user = new User();
        user.setId(1L);
        user.setUsername("owner");

        UserRequest request = new UserRequest("Sergio", "Perez", "sergio@example.com", "owner", "newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityUtil.extractUsername()).thenReturn("intruder");
        when(securityUtil.isAdmin()).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> userServiceImpl.updateUser(request, 1L));
    }

    @Test
    void shouldNotUpdateWhenNotFound() {

        UserRequest request = new UserRequest("Sergio", "Perez", "sergio@example.com", "owner", "newPassword");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.updateUser(request, 99L));
    }

    @Test
    void shouldDeleteSuccessfullyAsSelf() {

        User user = new User();
        user.setId(1L);
        user.setUsername("tornado");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityUtil.extractUsername()).thenReturn("tornado");

        userServiceImpl.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowForbiddenWhenDeletingWithWrongUser() {

        User user = new User();
        user.setId(1L);
        user.setUsername("owner");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityUtil.extractUsername()).thenReturn("intruder");
        when(securityUtil.isAdmin()).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> userServiceImpl.deleteUser(1L));
    }

    @Test
    void shouldNotDeleteWhenNotFound() {

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.deleteUser(99L));
    }
}
