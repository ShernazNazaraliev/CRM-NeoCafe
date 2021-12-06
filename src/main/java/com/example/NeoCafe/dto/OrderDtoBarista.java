package com.example.NeoCafe.dto;

import com.example.NeoCafe.Enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDtoBarista {
    private OrderTypes orderType;
    private List<OrderDetailsDto> listOrderDetailsDto;

}
