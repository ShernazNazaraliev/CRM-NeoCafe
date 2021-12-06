package com.example.NeoCafe.repository;

import com.example.NeoCafe.entity.Composition;
import com.example.NeoCafe.entity.Menu;
import com.example.NeoCafe.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {

    List<Composition> findAllByMenu(Menu menu);

}
