package com.example.shopapi.services;

import com.example.shopapi.dto.OrderDTO;
import com.example.shopapi.dto.OrderItemDTO;
import com.example.shopapi.dto.ProductDTO;
import com.example.shopapi.enums.OrderStatus;
import com.example.shopapi.enums.ProductCategory;
import com.example.shopapi.enums.ProductDepartment;
import com.example.shopapi.enums.Role;
import com.example.shopapi.exceptions.ObjectNotFoundException;
import com.example.shopapi.exceptions.StripeSessionNotFoundException;
import com.example.shopapi.mappers.OrderMapper;
import com.example.shopapi.mappers.ProductMapper;
import com.example.shopapi.models.*;
import com.example.shopapi.repositories.OrderRepository;
import com.example.shopapi.repositories.UserRepository;
import com.example.shopapi.services.impl.OrderServiceImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;
    @Mock
    StripeService stripeService;

    @InjectMocks
    OrderServiceImpl orderService;

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

    @Test
    void createOrder_Success() throws StripeException {
        Long userId = 1L;
        OrderDTO orderDTO = new OrderDTO();

        ProductDTO productDTO1 = ProductMapper.mapEntityToProductDTO(product1);
        ProductDTO productDTO2 = ProductMapper.mapEntityToProductDTO(product2);
        ProductDTO productDTO3 = ProductMapper.mapEntityToProductDTO(product3);

        OrderItemDTO orderItemDTO1 = OrderMapper.mapEntityToOrderItemDTO(orderItem1);
        OrderItemDTO orderItemDTO2 = OrderMapper.mapEntityToOrderItemDTO(orderItem2);
        OrderItemDTO orderItemDTO3 = OrderMapper.mapEntityToOrderItemDTO(orderItem3);

        orderDTO.setItems(new ArrayList<>(List.of(orderItemDTO1, orderItemDTO2, orderItemDTO3)));

        Session mockSession = new Session();
        mockSession.setId("test_id");
        mockSession.setClientSecret("test_secret");
        order.setStripeSessionId(mockSession.getId());
        order.setStripeClientSecret(mockSession.getClientSecret());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(stripeService
                .createCheckoutSession(new ArrayList<>(List.of(orderItemDTO1, orderItemDTO2, orderItemDTO3))))
                .willReturn(mockSession);
        given(orderRepository.save(any(Order.class))).willReturn(order);
        order.setOrderStatus(OrderStatus.PAID);

        OrderDTO createdOrderDTO = orderService.createOrder(orderDTO, userId);

        assertThat(createdOrderDTO.getOrderStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(createdOrderDTO.getItems()).isEqualTo(orderDTO.getItems());
        assertThat(createdOrderDTO.getStripeClientSecret()).isEqualTo(mockSession.getClientSecret());
    }

    @Test
    void createOrder_UserDoesNotExist() {
        Long userId = 1L;
        OrderDTO orderDTO = new OrderDTO();

        ProductDTO productDTO1 = ProductMapper.mapEntityToProductDTO(product1);
        ProductDTO productDTO2 = ProductMapper.mapEntityToProductDTO(product2);
        ProductDTO productDTO3 = ProductMapper.mapEntityToProductDTO(product3);

        OrderItemDTO orderItemDTO1 = OrderMapper.mapEntityToOrderItemDTO(orderItem1);
        OrderItemDTO orderItemDTO2 = OrderMapper.mapEntityToOrderItemDTO(orderItem2);
        OrderItemDTO orderItemDTO3 = OrderMapper.mapEntityToOrderItemDTO(orderItem3);

        orderDTO.setItems(new ArrayList<>(List.of(orderItemDTO1, orderItemDTO2, orderItemDTO3)));

        given(userRepository.findById(userId)).willReturn(Optional.empty());



        assertThatThrownBy(() -> orderService.createOrder(orderDTO, userId)).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1");

        then(orderRepository).should(never()).save(any(Order.class));
    }

    @Test
    void updateOrder_OrderDoesNotExist() {
        String sessionId = "abc";

        given(orderRepository.findByStripeSessionId(sessionId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderWithPaymentStatus(sessionId)).isInstanceOf(StripeSessionNotFoundException.class)
                .hasMessage("Could not find " + "order" + "with session id " + sessionId);

        then(orderRepository).should(never()).save(any(Order.class));
    }
}
