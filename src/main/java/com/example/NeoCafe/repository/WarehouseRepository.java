package com.example.NeoCafe.repository;

import com.example.NeoCafe.Enums.EWarehouseCategory;
import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository <Warehouse,Long>{

    Optional<Warehouse> findByProductId(Long id);

    boolean existsByProductName(String name);

    List<Warehouse> findAllByStatusAndWarehouseCategory(Status activate, EWarehouseCategory eWarehouseCategory);

    @Query("SELECT e FROM Warehouse e WHERE e.quantity<e.minNumber and e.status='ACTIVATE'")
    List<Warehouse> findAllEndingProducts();

}
