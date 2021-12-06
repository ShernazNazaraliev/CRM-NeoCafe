package com.example.NeoCafe.dto;

import com.example.NeoCafe.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeStatusDto {

    private long orderId;
    private OrderStatus orderStatus;
}
