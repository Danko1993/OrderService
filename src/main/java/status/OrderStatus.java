package status;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CART("Cart products."),
    PREPARED("Order is beeing prepared."),
    SHIPPED("Order has been shipped."),
    DELIVERED("Order has been delivered."),
    CANCELLED("Order has been canceled.");

    private final String message;

    OrderStatus(String message) {
        this.message = message;
    }
}
