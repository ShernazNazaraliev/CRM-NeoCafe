package com.example.NeoCafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UpdateOrderDetailsDTO {
    private Long orderId;
    private OrderDetailsDto orderDetailsDto;
}
