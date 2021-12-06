package com.example.NeoCafe.repository;

import com.example.NeoCafe.Enums.ERole;
import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.entity.Branches;
import com.example.NeoCafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findAllByStatusAndBranchesAndRole(Status status, Branches branches,ERole role);
    List<User> findAllByStatusAndBranchesAndRoleOrderByRatingDesc(Status status, Branches branches,ERole role);

    User getByBranchesAndRole(Branches branches, ERole role);
}