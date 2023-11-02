package com.example.shopapi.services.impl;

import com.example.shopapi.dto.UserRequestDTO;
import com.example.shopapi.dto.UserResponseDTO;
import com.example.shopapi.dto.UserUpdateDTO;
import com.example.shopapi.enums.Role;
import com.example.shopapi.exceptions.DuplicateEmailException;
import com.example.shopapi.exceptions.ObjectNotFoundException;
import com.example.shopapi.mappers.UserMapper;
import com.example.shopapi.models.User;
import com.example.shopapi.repositories.UserRepository;
import com.example.shopapi.services.TokenService;
import com.example.shopapi.services.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService,
                           @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        boolean emailAlreadyExists = userRepository.findByEmail(userRequestDTO.getEmail()).isPresent();
        if (emailAlreadyExists) {
            throw new DuplicateEmailException("A user with that email address already exists");
        }
        User user = UserMapper.mapUserRequestDTOtoEntity(userRequestDTO);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return UserMapper.mapEntityToUserResponseDTO(savedUser);
    }
    @Override
    public UserResponseDTO loginUser(UserRequestDTO userRequestDTO) {
        User user = userRepository.findByEmail(userRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(userRequestDTO.getEmail(), userRequestDTO.getPassword())
                );

        UserResponseDTO userResponseDTO = UserMapper.mapEntityToUserResponseDTO(user);
        String jwt = tokenService.generateJwt(auth, user.getId());
        userResponseDTO.setJwt(jwt);
        return userResponseDTO;
    }
    @Override
    public UserResponseDTO updateUser(UserUpdateDTO userUpdateDTO, Long id) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("user", id));
        User updatedUser = UserMapper.mapUserUpdateDTOtoEntity(userUpdateDTO);
        existingUser.setName(updatedUser.getName());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAddress(updatedUser.getAddress());
        if (userRepository.findByEmail(existingUser.getEmail()).isPresent()) {
            throw new DuplicateEmailException("A user with that email address already exists.");
        }
        User savedUser = userRepository.save(existingUser);
        return UserMapper.mapEntityToUserResponseDTO(savedUser);
    }
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ObjectNotFoundException("user", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }
}
