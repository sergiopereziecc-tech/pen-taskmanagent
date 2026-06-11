package com.pen.taskmanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;
import com.pen.taskmanagement.exceptions.ForbiddenException;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.UserMapper;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.UserRepository;
import com.pen.taskmanagement.utilities.SecurityUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);

        user.setPassword(passwordEncoder.encode(userRequest.password()));
        return userMapper.toResponse(userRepository.save(user));

    }

    @Override
    public Page<UserResponse> readAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);

        
    }

    @Override
    public UserResponse readUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User not found");
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getUsername().equals(securityUtil.extractUsername())) {
            userRepository.deleteById(id);
        } else {
            throw new ForbiddenException("Permissions not found");
        }

    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getUsername().equals(securityUtil.extractUsername())) {
            user.setName(userRequest.name());
            user.setSurname(userRequest.surname());
            user.setEmail(userRequest.email());
            user.setUsername(userRequest.username());
            user.setPassword(passwordEncoder.encode(userRequest.password()));

        } else {
            throw new ForbiddenException("Permissions not found");
        }

        return userMapper.toResponse(userRepository.save(user));

    }

}
