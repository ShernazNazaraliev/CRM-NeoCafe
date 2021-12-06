package com.example.NeoCafe.dto;

import com.example.NeoCafe.Enums.OrderStatus;
import com.example.NeoCafe.Enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TypeAndStatusForBaristaOrderDto {

    private OrderStatus orderStatus;
    private OrderTypes orderTypes;
}
