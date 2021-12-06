package com.example.NeoCafe.repository;

import com.example.NeoCafe.entity.Notifications;
import com.example.NeoCafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findAllByUser(User user);
}
