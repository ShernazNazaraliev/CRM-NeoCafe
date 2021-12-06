package com.example.NeoCafe.repository;

import com.example.NeoCafe.entity.GeneralAdditional;
import com.example.NeoCafe.entity.Menu;
import com.example.NeoCafe.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralAdditionalRepository extends JpaRepository<GeneralAdditional, Long> {

    List<GeneralAdditional> findAllByMenu(Menu menu);

}
