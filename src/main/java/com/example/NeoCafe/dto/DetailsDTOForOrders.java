package com.example.NeoCafe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class DetailsDTOForOrders {
    private String name;
    private double price;
    private String urlImage;
}
