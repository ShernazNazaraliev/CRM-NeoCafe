package com.example.NeoCafe.dto;

import com.example.NeoCafe.Enums.OrderStatus;
import com.example.NeoCafe.Enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDtoForWaiter {

    private Long id;

    private OrderStatus orderStatus;

    private Long tableId;

    private Date orderTime;

    private OrderTypes orderTypes;



}
