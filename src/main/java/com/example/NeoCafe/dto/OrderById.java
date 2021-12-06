package com.example.NeoCafe.dto;

import com.example.NeoCafe.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderById {
    private Long orderId;

    private List<OrderDetailsForWaiterDto> orderDetailsForWaiterDto;

    private Long tableId;

    private OrderStatus orderStatus;

    private Date dateOfOrder;

//    private String nameClient;

    private double totalPrice;

}
