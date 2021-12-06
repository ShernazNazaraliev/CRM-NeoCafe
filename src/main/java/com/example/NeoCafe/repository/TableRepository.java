package com.example.NeoCafe.repository;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.Enums.TableStatus;
import com.example.NeoCafe.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Tables, Long> {

    List<Tables> findAllByUser(User user);

    boolean existsByQrCode(String qr_code);

    List<Tables> findAllByStatus(Status status);

    Tables findByUser_IdAndId(Long id,long tableId);

    Tables findByIdAndUserAndTableStatus(Long id, User user,TableStatus tableStatus);
}
