package com.example.NeoCafe.dto;

import com.example.NeoCafe.entity.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class EmployeeForAdmin {
    private long id;
    private String name;
    private String role;
    private String branch;
    private String phoneNumber;
    private TimeTable timeTable;
    private List<Long> tablesId;
}
