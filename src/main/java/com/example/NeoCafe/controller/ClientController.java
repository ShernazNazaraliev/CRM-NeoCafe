package com.example.NeoCafe.controller;

import com.example.NeoCafe.dto.*;
import com.example.NeoCafe.entity.Category;
import com.example.NeoCafe.entity.Menu;
import com.example.NeoCafe.exception.UserNotFoundException;
import com.example.NeoCafe.jwt.JwtUtils;
import com.example.NeoCafe.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {

    private final UserDetailsServiceImpl userService;

    private final UserDetailsServiceImpl userServiceImpl;

    private final SmsService smsService;

    private final AuthenticationManager authenticationManager;

    private final CategoryService categoryService;

    private final OrdersService ordersService;

    private final MenuService menuService;

    private final TableService tableService;

    private final JwtUtils jwtUtils;

    private final BranchTimeTableService branchTimeTableService;


    public ClientController(TableService tableService, UserDetailsServiceImpl userService,
                            UserDetailsServiceImpl userServiceImpl, SmsService smsService,
                            AuthenticationManager authenticationManager, CategoryService categoryService,
                            OrdersService ordersService, MenuService menuService, JwtUtils jwtUtils,
                            BranchTimeTableService branchTimeTableService) {
        this.tableService = tableService;
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
        this.smsService = smsService;
        this.authenticationManager = authenticationManager;
        this.categoryService = categoryService;
        this.ordersService = ordersService;
        this.menuService = menuService;
        this.jwtUtils = jwtUtils;
        this.branchTimeTableService = branchTimeTableService;
    }

    @ApiOperation(value = "регистрация для клиентов")
    @PostMapping("/registration")
    public void sendUser(@RequestBody RegisterClient client) throws Exception {
        if(!userService.existsByPhoneNumber(client.getPhoneNumber())){
            smsService.send(client);
        }
        else {
            if (!userService.checkPhoneNumber(client.getPhoneNumber()))
                smsService.send(client);
            else
                throw new Exception("Пользователь уже зарегистрирован!");
        }
    }

    @ApiOperation(value = "подтверждение номера при регистрации")
    @PostMapping("/activate")
    public ResponseEntity<AuthenticationResponse> activate(@RequestBody UserDto userDto) throws Exception {
        userService.activate(userDto);
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

    @ApiOperation(value = "Дата рождение клиента")
    @PutMapping("/add-bDate")
    public void addBDate(@RequestBody BDate bDate) throws Exception {
        userService.setBDateClient(bDate.getBDate());
    }

    @ApiOperation(value = "Запрос 4 значного кода при авторизации")
    @PostMapping("/auth")
    public void auth(@RequestBody PhoneNumber phoneNumber) {
        if (userServiceImpl.auth(phoneNumber.getPhoneNumber())){
            smsService.send_auth(phoneNumber.getPhoneNumber());
        }
        else
            throw new UserNotFoundException();
    }

    @ApiOperation(value = "Авторизация клиента")
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

    @ApiOperation(value = "Получение количества бонусов текущего пользователя (клиента)")
    @GetMapping("/bonuses/get-amount")
    public Long getBonuses(){
        return ordersService.getBonuses();
    }

    @ApiOperation(value = "Получение всех категорий меню")
    @GetMapping("/category/all")
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @ApiOperation(value = "Получение популярных блюд")
    @GetMapping("/menu/get-popular")
    public List<Menu> getPopularStock(){
        return menuService.topStock();
    }

    @ApiOperation(value = "получение всех блюд по категории")
    @GetMapping("/menu/all/by-category/{categoryId}")
    public List<MenuForClient> getAllByCategoryIdClient(@PathVariable long categoryId) throws Exception {
        return menuService.allMenuByCategoryForClient(categoryId); //TODO: check this dto
    }

    @ApiOperation(value = "Получение блюда по айди")
    @GetMapping("/menu/item-info/{itemId}")
    public ResponseEntity<MenuForClient> getMenuById(@PathVariable Long itemId) throws Exception {
        return new ResponseEntity<>(menuService.getOneMenuForAdmin(itemId),HttpStatus.OK); //TODO: check this dto
        //TODO: Test this function in case item has additional things and in case it doesn't
    }

    @ApiOperation(value = "Использовать бонусы текущего клиента")
    @PostMapping("/bonuses/subtract/{amount}")
    public Long subtract(@PathVariable Long amount){
        return ordersService.subtractBonuses(amount);
    }

    @ApiOperation(value = "Проверка стола на занятость")
    @GetMapping("/table/is-free/{id}")
    public boolean getTableStatus(@PathVariable(value = "id") Long id) throws Exception {
        return tableService.isFree(id);
    }

    @ApiOperation(value = "Добавление (оформление) заказов для клиента")
    @PostMapping("/orders/add")
    public ResponseEntity<OrderDto> addNewOrder(@RequestBody OrderDto orderDto) throws Exception {
        return new ResponseEntity<>(ordersService.addOrderForClient(orderDto), HttpStatus.OK);
    }

    @ApiOperation(value = "получить текущий заказ current клиента")
    @GetMapping("/orders/get-curr-order")
    public List<OrderCurrAndCompletedForClientDto> getCurrentOrder(){
        return ordersService.getCurrentOrderClean();
    }

    @ApiOperation(value = "получить все завершенные заказы current клиента")
    @GetMapping("/orders/get-completed-orders")
    public List<OrderCurrAndCompletedForClientDto> getCompletedOrders(){
        return ordersService.getCompletedOrdersClean();
    }

    @ApiOperation("Получение сегодняшней информации о филиалах для клиента (используйте это)")
    @GetMapping("/branches/get-all-up-to-date-info")
    public List<BranchWorkingTimeDto> getAllInfo(){
        return branchTimeTableService.getTodayInfo();
    }

    @ApiOperation(value = "Редактирование профиля клиента")
    @PostMapping("/profile/edit")
    public void editProfile(@RequestBody ClientDataDto dto) throws Exception {
        userService.updateDataClient(dto);
    }

    @GetMapping("/profile/info")
    public ClientInfoDto getPersonalData(){
        return userService.getPersonalData();
    }
}
