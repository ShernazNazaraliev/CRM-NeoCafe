package com.example.NeoCafe.dto;

import com.example.NeoCafe.Enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private OrderTypes orderType;
    private Long branchId;
    private Long tableId;
    private List<OrderDetailsDto> listOrderDetailsDto;

}
