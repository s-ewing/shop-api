package com.example.shopapi.services;

import com.example.shopapi.dto.AddressDTO;
import com.example.shopapi.dto.UserRequestDTO;
import com.example.shopapi.dto.UserResponseDTO;
import com.example.shopapi.dto.UserUpdateDTO;
import com.example.shopapi.enums.OrderStatus;
import com.example.shopapi.enums.ProductCategory;
import com.example.shopapi.enums.ProductDepartment;
import com.example.shopapi.enums.Role;
import com.example.shopapi.exceptions.DuplicateEmailException;
import com.example.shopapi.exceptions.ObjectNotFoundException;
import com.example.shopapi.mappers.AddressMapper;
import com.example.shopapi.mappers.OrderMapper;
import com.example.shopapi.models.*;
import com.example.shopapi.repositories.UserRepository;
import com.example.shopapi.services.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    TokenService tokenService;

    @InjectMocks
    UserServiceImpl userService;
    Product product1 = new Product();
    Product product2 = new Product();
    Product product3 = new Product();
    OrderItem orderItem1 = new OrderItem();
    OrderItem orderItem2 = new OrderItem();
    OrderItem orderItem3 = new OrderItem();
    List<OrderItem> items = new ArrayList<>(List.of(orderItem1, orderItem2, orderItem3));
    Order order = new Order();
    Address address = new Address();
    User user = new User();

    @BeforeEach
    void setUp() {
        product1.setId(1L);
        product1.setName("productName");
        product1.setDescription("productDescription");
        product1.setPrice(BigDecimal.valueOf(1));
        product1.setImgSrc("productImgSrc");
        product1.setCategories(new ArrayList<>(List.of(ProductCategory.SHOES)));
        product1.setDepartments(new ArrayList<>(List.of(ProductDepartment.MENS)));

        product2.setId(2L);
        product2.setName("productName");
        product2.setDescription("productDescription");
        product2.setPrice(BigDecimal.valueOf(2));
        product2.setImgSrc("productImgSrc");
        product2.setCategories(new ArrayList<>(List.of(ProductCategory.SHOES)));
        product2.setDepartments(new ArrayList<>(List.of(ProductDepartment.WOMENS)));

        product3.setId(3L);
        product3.setName("productName");
        product3.setDescription("productDescription");
        product3.setPrice(BigDecimal.valueOf(3));
        product3.setImgSrc("productImgSrc");
        product3.setCategories(new ArrayList<>(List.of(ProductCategory.HATS)));

        orderItem1.setId(1L);
        orderItem1.setProduct(product1);
        orderItem1.setQuantity(1);

        orderItem2.setId(2L);
        orderItem2.setProduct(product2);
        orderItem2.setQuantity(2);

        orderItem3.setId(3L);
        orderItem3.setProduct(product3);
        orderItem3.setQuantity(3);

        List<OrderItem> items = new ArrayList<>(List.of(orderItem1, orderItem2, orderItem3));

        order.setId(1L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTimePlaced(LocalDateTime.now());
        order.setItems(items);

        orderItem1.setOrder(order);
        orderItem2.setOrder(order);
        orderItem3.setOrder(order);

        address.setStreetAddress("Street Address");
        address.setCity("City");
        address.setState("State");
        address.setZipCode("12345");

        user.setId(1L);
        user.setEmail("test@email.com");
        user.setPassword("testPassword");
        user.setRole(Role.USER);
        user.setName("User Name");
        user.setOrders(new ArrayList<>(List.of(order)));
        user.setAddress(address);

        order.setUser(user);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser_Success() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@email.com");
        userRequestDTO.setPassword("testPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@email.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.USER);

        given(userRepository.findByEmail(userRequestDTO.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(userRequestDTO.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        then(userRepository).should().save(any(User.class));

        assertThat(userResponseDTO.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(userResponseDTO.getName()).isNull();
        assertThat(userResponseDTO.getAddress()).isNull();
        assertThat(userResponseDTO.getOrders()).isNull();
        assertThat(userResponseDTO.getJwt()).isNull();
    }

    @Test
    void createUser_DuplicateEmail() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@email.com");
        userRequestDTO.setPassword("testPassword");

        given(userRepository.findByEmail(userRequestDTO.getEmail())).willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.createUser(userRequestDTO))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage("A user with that email address already exists");

        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@email.com");
        userRequestDTO.setPassword("testPassword");

        given(userRepository.findByEmail(userRequestDTO.getEmail())).willReturn(Optional.of(user));

        Authentication auth = mock(Authentication.class);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(auth);

        String jwt = "testJwt";
        given(tokenService.generateJwt(auth, user.getId())).willReturn(jwt);

        UserResponseDTO userResponseDTO = userService.loginUser(userRequestDTO);

        assertThat(userResponseDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponseDTO.getName()).isEqualTo(user.getName());
        assertThat(userResponseDTO.getAddress()).isEqualTo(AddressMapper.mapEntityToAddressDTO(user.getAddress()));
        assertThat(userResponseDTO.getOrders()).isEqualTo(user.getOrders().stream().map(OrderMapper::mapEntityToOrderDTO).collect(Collectors.toList()));
        assertThat(userResponseDTO.getJwt()).isEqualTo(jwt);
    }

    @Test
    void loginUser_InvalidEmail() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@email.com");
        userRequestDTO.setPassword("testPassword");

        assertThatThrownBy(() -> userService.loginUser(userRequestDTO)).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Bad credentials");

        then(tokenService).should(never()).generateJwt(any(Authentication.class), any(Long.class));
    }

    @Test
    void updateUser_Success() {
        AddressDTO updatedAddressDTO = new AddressDTO();
        updatedAddressDTO.setStreetAddress("Updated Address");
        updatedAddressDTO.setCity("Updated City");
        updatedAddressDTO.setState("Updated State");
        updatedAddressDTO.setZipCode("54321");

        Long id = 1L;
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName("Updated Name");
        userUpdateDTO.setEmail("updated@email.com");
        userUpdateDTO.setPassword("updatedPassword");
        userUpdateDTO.setAddress(updatedAddressDTO);

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setName(userUpdateDTO.getName());
        updatedUser.setEmail(userUpdateDTO.getEmail());
        updatedUser.setAddress(AddressMapper.mapAddressDTOtoEntity(userUpdateDTO.getAddress()));

        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(updatedUser);

        UserResponseDTO userResponseDTO = userService.updateUser(userUpdateDTO, id);

        assertThat(user.getName()).isEqualTo(userUpdateDTO.getName());
        assertThat(user.getEmail()).isEqualTo(userUpdateDTO.getEmail());
        assertThat(user.getAddress()).isEqualTo(AddressMapper.mapAddressDTOtoEntity(userUpdateDTO.getAddress()));

        assertThat(userResponseDTO.getName()).isEqualTo(userUpdateDTO.getName());
        assertThat(userResponseDTO.getEmail()).isEqualTo(userUpdateDTO.getEmail());
        assertThat(userResponseDTO.getAddress()).isEqualTo(userUpdateDTO.getAddress());
    }

    @Test
    void updateUser_UserDoesNotExist() {
        Long id = 10L;
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName("Updated Name");
        userUpdateDTO.setEmail("updated@email.com");
        userUpdateDTO.setPassword("updatedPassword");

        given(userRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userUpdateDTO, id)).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 10");

        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    void updateUser_DuplicateEmail() {
        Long id = 1L;
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName("Updated Name");
        userUpdateDTO.setEmail("updated@email.com");
        userUpdateDTO.setPassword("updatedPassword");

        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.findByEmail(userUpdateDTO.getEmail())).willReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updateUser(userUpdateDTO, id)).isInstanceOf(DuplicateEmailException.class)
                .hasMessage("A user with that email address already exists.");

        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        Long userId = 1L;

        given(userRepository.existsById(userId)).willReturn(true);

        userService.deleteUser(userId);

        then(userRepository).should().deleteById(userId);
    }

    @Test
    void deleteUser_UserDoesNotExist() {
        Long userId = 1L;

        given(userRepository.existsById(userId)).willReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1");

        then(userRepository).should(never()).deleteById(any(Long.class));
    }
}