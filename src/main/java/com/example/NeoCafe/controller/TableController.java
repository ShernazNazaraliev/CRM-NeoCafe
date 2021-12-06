package com.example.NeoCafe.controller;


import com.example.NeoCafe.dto.TablesDtoForWaiter;
import com.example.NeoCafe.service.TableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("table")
public class TableController {
    
    @Autowired
    private TableService tableService;

    @ApiOperation(value = "получение столов текущего пользователя(официанта)")
    @GetMapping("get-tables-for-current-user")
    public List<TablesDtoForWaiter> getTablesForCurrentUser(){
        return tableService.allTablesByUser();
    }

    @GetMapping("/is-free/{id}")
    public boolean getTableStatus(@PathVariable(value = "id") Long id) throws Exception {
        return tableService.isFree(id);
    }
}