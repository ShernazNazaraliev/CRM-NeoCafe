package com.example.NeoCafe.repository;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByStatus(Status status);

    boolean existsByName(String name);
}
