package com.example.shopapi.mappers;

import com.example.shopapi.dto.UserRequestDTO;
import com.example.shopapi.dto.UserResponseDTO;
import com.example.shopapi.dto.UserUpdateDTO;
import com.example.shopapi.models.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static User mapUserRequestDTOtoEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        return user;
    }

    public static User mapUserUpdateDTOtoEntity(UserUpdateDTO userUpdateDTO) {
        User updatedUser = new User();

        updatedUser.setName(userUpdateDTO.getName());
        updatedUser.setPassword(userUpdateDTO.getPassword());
        updatedUser.setEmail(userUpdateDTO.getEmail());

        if (userUpdateDTO.getAddress() != null) {
            updatedUser.setAddress(AddressMapper.mapAddressDTOtoEntity(userUpdateDTO.getAddress()));
        }

        return updatedUser;
    }

    public static UserResponseDTO mapEntityToUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());

        if (user.getAddress() != null) {
            userResponseDTO.setAddress(AddressMapper.mapEntityToAddressDTO(user.getAddress()));
        }

        if (user.getOrders() != null) {
            userResponseDTO.setOrders(user.getOrders().stream().map(OrderMapper::mapEntityToOrderDTO).collect(Collectors.toList()));
        }

        return userResponseDTO;
    }

}
