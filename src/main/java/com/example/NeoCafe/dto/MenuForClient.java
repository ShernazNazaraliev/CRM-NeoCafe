package com.example.NeoCafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class MenuForClient {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private List<GeneralDTO> generalAdditional;
}
