package com.example.NeoCafe.repository;

import com.example.NeoCafe.Enums.OrderStatus;
import com.example.NeoCafe.Enums.OrderTypes;
import com.example.NeoCafe.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllByEmployee_Id(Long Id);

    List<Orders> findAllByEmployeeAndOrderStatus(User user, OrderStatus orderStatus);

    @Query("SELECT e FROM Orders e WHERE e.client.id =:idClient ORDER BY e.orderTime DESC")
    List<Orders> orderHistory(Long idClient);

    List<Orders> findAllByBranchAndOrderStatusAndOrderType(Branches branches,
                                                           OrderStatus orderStatus,
                                                           OrderTypes orderTypes);

    List<Orders> findAllByClientIdAndOrderStatusOrOrderStatusOrOrderStatus(Long clientId,
                                                 OrderStatus status1, OrderStatus status2, OrderStatus status3);
    List<Orders> findAllByClientIdAndOrderStatusOrderByOrderTimeDesc(Long clientId, OrderStatus status1);

    Orders findByTable_IdAndEmployee_Id(long tableId, long emp_id);
}
