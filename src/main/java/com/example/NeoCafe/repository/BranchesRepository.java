package com.example.NeoCafe.repository;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.entity.Branches;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchesRepository extends JpaRepository<Branches,Long> {

    boolean existsByName(String name);

    Branches getByName(String name);

    List<Branches> findAllByStatus(Status status);
}
