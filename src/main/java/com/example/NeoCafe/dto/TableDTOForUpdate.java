package com.example.NeoCafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TableDTOForUpdate {

    private Long tableId;
    private int tableStatus;
    private String qrCode;
    private Long branchId;
    private Long userId;
}
