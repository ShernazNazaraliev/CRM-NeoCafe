package com.example.NeoCafe.controller;

import com.example.NeoCafe.dto.OrderById;
import com.example.NeoCafe.dto.OrderDetailsForWaiterDto;
import com.example.NeoCafe.dto.OrderTypesDto;
import com.example.NeoCafe.service.OrderDetailsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("Order-details")
@RestController
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @ApiOperation(value = "получение деталей одного заказа по типу заказа(на вынос или в) для официанта и баристы")
    @PostMapping("get-all-byOrder/{orderId}")
    public List<OrderDetailsForWaiterDto> getAllDetailsByOrder(@PathVariable Integer orderId, @RequestBody OrderTypesDto orderTypes){
        return orderDetailsService.allOrderDetailsByOrders(orderId,orderTypes.getOrderTypes());
    }

    @ApiOperation(value = "получение иннфы о заказе по айдишке стола")
    @GetMapping("get-all-details-by-table/{tableId}")
    public OrderById getAllOrderDetailsByOrderId(@PathVariable Long tableId){
        return orderDetailsService.getAllDetailsByTable(tableId);
    }
}
