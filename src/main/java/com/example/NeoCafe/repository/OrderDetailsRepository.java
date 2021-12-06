package com.example.NeoCafe.repository;

import com.example.NeoCafe.Enums.OrderTypes;
import com.example.NeoCafe.entity.OrderDetails;
import com.example.NeoCafe.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    List<OrderDetails> findAllByOrders(Orders orders);

    List<OrderDetails> findAllByOrdersAndOrders_OrderType(Orders orders, OrderTypes orderTypes);
}
