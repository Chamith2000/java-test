package com.newsportal.service.impl;

import com.newsportal.dto.RegisterRequest;
import com.newsportal.entity.Role;
import com.newsportal.entity.User;
import com.newsportal.exception.DuplicateResourceException;
import com.newsportal.exception.ResourceNotFoundException;
import com.newsportal.repository.RoleRepository;
import com.newsportal.repository.UserRepository;
import com.newsportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerNewReader(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered");
        }

        Role readerRole = roleRepository.findByName(Role.ERole.ROLE_READER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role ROLE_READER not found"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of(readerRole))
                .build();

        return userRepository.save(user);
    }
}
