package com.example.shopapi.controllers;

import com.example.shopapi.dto.UserRequestDTO;
import com.example.shopapi.dto.UserResponseDTO;
import com.example.shopapi.dto.UserUpdateDTO;
import com.example.shopapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final JwtDecoder jwtDecoder;

    public UserController(UserService userService, JwtDecoder jwtDecoder){
        this.userService = userService;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/new")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.loginUser(userRequestDTO);
        if (userResponseDTO != null) {
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                                      @PathVariable Long id,
                                                      @RequestHeader(name="Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        Long userId = Long.parseLong(jwtDecoder.decode(jwt).getSubject());
        if (!userId.equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(userService.updateUser(userUpdateDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("",HttpStatus.NO_CONTENT);
    }
}
