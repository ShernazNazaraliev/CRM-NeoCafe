package com.example.NeoCafe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class HistoryForClient {

    private Long id;
    private String name;
    private double price;
    private double quantity;

}
