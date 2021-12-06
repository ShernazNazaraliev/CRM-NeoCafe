package com.example.NeoCafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaiterTableDto {

    private List<Long> tablesId;

    private String waiterPhoneNumber;
}
