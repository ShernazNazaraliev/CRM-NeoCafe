package com.example.NeoCafe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data

public class NotificationsDTO {

    private Long id;
    private String title;
    private String message;
    private Date time;
}
