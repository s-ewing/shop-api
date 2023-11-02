package com.example.shopapi.services;

import com.example.shopapi.dto.UserRequestDTO;
import com.example.shopapi.dto.UserResponseDTO;
import com.example.shopapi.dto.UserUpdateDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO loginUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(UserUpdateDTO userUpdateDTO, Long id);
    void deleteUser(Long id);
}
