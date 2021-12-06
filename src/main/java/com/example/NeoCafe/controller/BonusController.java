package com.example.NeoCafe.controller;

import com.example.NeoCafe.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/bonuses")
public class BonusController {

    @Autowired
    OrdersService ordersService;

    @GetMapping("/get-amount")
    public Long getBonuses(){
        return ordersService.getBonuses();
    }

    @PostMapping("/subtract/{amount}")
    public Long subtract(@PathVariable Long amount){
        return ordersService.subtractBonuses(amount);
    }
}
