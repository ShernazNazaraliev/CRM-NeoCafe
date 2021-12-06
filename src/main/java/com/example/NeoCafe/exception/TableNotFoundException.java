package com.example.NeoCafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TableNotFoundException extends RuntimeException{

    public TableNotFoundException() {
        super("Table not found");
    }

}
