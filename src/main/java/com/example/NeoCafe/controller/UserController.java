package com.example.NeoCafe.controller;

import com.example.NeoCafe.dto.*;
import com.example.NeoCafe.entity.TimeTable;
import com.example.NeoCafe.jwt.JwtUtils;
import com.example.NeoCafe.service.SmsService;
import com.example.NeoCafe.service.UserDetailsServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final SmsService smsService;

    private final UserDetailsServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public UserController(SmsService smsService, UserDetailsServiceImpl userServiceImpl, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.smsService = smsService;
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @ApiOperation(value = "Запрос 4 значного кода при авторизации")
    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody PhoneNumber phoneNumber) {
        if (userServiceImpl.auth(phoneNumber.getPhoneNumber())){
            smsService.send_auth(phoneNumber.getPhoneNumber());
            return ResponseEntity.ok("Код отправлен на указанный номер телефона");
        }
        return ResponseEntity.ok("Текущий номер не зарегистрирован!");
    }

    @ApiOperation(value = "Авторизация пользователей")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserDto userDto) throws Exception {
        if (userServiceImpl.auth(userDto.getPhoneNumber())) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userDto.getPhoneNumber(),
                                userDto.getCode())
                );
            } catch (BadCredentialsException e) {
                throw new Exception("Неверный код!", e);
            }
            final UserDetails userDetails = userServiceImpl.loadUserByUsername(userDto.getPhoneNumber());
            return new ResponseEntity<>(jwtUtils.generateToken(userDetails),HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation("проверка сотрудника на первую авторизацю")
    @GetMapping("/check-completed")
    public ResponseEntity<CheckCompletedDTO> checkIsCompleted(){
        return new ResponseEntity<>(userServiceImpl.checkCompleted(),HttpStatus.OK);
    }

    @ApiOperation("страница после первой авторизации сотрудников")
    @PutMapping("/personalData")
    public void updateUser(@RequestBody DataDto model) throws Exception {
        userServiceImpl.updateData(model);
    }

    @GetMapping("/time-table-current-user")
    public ResponseEntity<TimeTable> getTimeTableUser(){
        return new ResponseEntity<>(userServiceImpl.getTimeTable(), HttpStatus.OK);
    }

    @GetMapping("/get-rating")
    public RatingDto getRating(){
        return userServiceImpl.getTopRaiting();
    }

}