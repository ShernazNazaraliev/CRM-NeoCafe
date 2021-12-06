package com.example.NeoCafe.repository;

import com.example.NeoCafe.entity.Additionals;
import com.example.NeoCafe.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalsRepository extends JpaRepository<Additionals,Long> {

    List<Additionals> findAllByOrderDetail(OrderDetails orderDetails);

    List<Additionals> findAllByOrderDetailId(Long orderDetailsId);

}
