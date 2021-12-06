package com.example.NeoCafe.dto;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.Enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TablesDtoForWaiter {

   private Long tableId;
   private TableStatus status;
}
