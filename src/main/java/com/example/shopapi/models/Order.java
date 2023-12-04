package com.example.shopapi.models;

import com.example.shopapi.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_order")
public class Order {
    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private Long id;
    @NotEmpty(message = "Orders must contain at least 1 item")
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private LocalDateTime timePlaced;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(List<OrderItem> items, OrderStatus orderStatus, LocalDateTime timePlaced, User user) {
        this.items = items;
        this.orderStatus = orderStatus;
        this.timePlaced = timePlaced;
        this.user = user;
    }

    public BigDecimal getTotal() {
        if (!this.items.isEmpty()) {
            return items.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            return new BigDecimal(0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
