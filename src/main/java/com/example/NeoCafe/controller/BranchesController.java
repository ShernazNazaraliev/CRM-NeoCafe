package com.example.NeoCafe.controller;

import com.example.NeoCafe.dto.BranchWorkingTimeDto;
import com.example.NeoCafe.entity.Branches;
import com.example.NeoCafe.service.BranchTimeTableService;
import com.example.NeoCafe.service.BranchesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/branches")
public class BranchesController {

    private final BranchesService branchesService;

    private final BranchTimeTableService branchTimeTableService;

    public BranchesController(BranchesService branchesService, BranchTimeTableService branchTimeTableService) {
        this.branchesService = branchesService;
        this.branchTimeTableService = branchTimeTableService;
    }

    @ApiOperation("Получение полной информации о филиалах (на все 7 дней)")
    @GetMapping("/get-all-full")
    public List<Branches> getAll(){
        return branchesService.getAllForClient();
    }

    @ApiOperation("Получение сегодняшней информации о филиалах для клиента (используйте это)")
    @GetMapping("/get-all-up-to-date-info")
    public List<BranchWorkingTimeDto> getAllInfo(){
        return branchTimeTableService.getTodayInfo();
    }

}
