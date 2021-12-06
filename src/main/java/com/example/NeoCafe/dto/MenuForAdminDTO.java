package com.example.NeoCafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class MenuForAdminDTO {

    private long id;
    private String menuName;
    private String category;
    private List<String> composition;
    private double price;
}
