package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import status.OrderStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Order {

    @Id
    private UUID id=UUID.randomUUID();
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products;
    @NonNull
    private double orderPrice;
    @NonNull
    private double orderWeight;
    @NonNull
    private OrderStatus status;
    @NonNull
    private String deliveryAddress;

    private Date estimatedDeliveryDate;

    private String customerName;
}
