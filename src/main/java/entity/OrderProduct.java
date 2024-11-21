package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class OrderProduct {

    @Id
    UUID id = UUID.randomUUID();
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    @NonNull
    UUID productId;
    @NonNull
    double unitPrice;
    @NonNull
    double unitWeight;
    @NonNull
    int orderedQuantity;
    @NonNull
    double totalPrice;
    @NonNull
    double totalWeight;


    public void setTotalPrice(Double unitPrice, Integer orderedQuantity){
        this.totalPrice = unitPrice*orderedQuantity;
    }
    public void setTotalWeight(Double unitWeight, Integer orderedQuantity){
        this.totalWeight = unitWeight * orderedQuantity;
    }

}
