package com.example.NeoCafe.controller;

import com.example.NeoCafe.dto.*;
import com.example.NeoCafe.entity.Branches;
import com.example.NeoCafe.exception.AlreadyExistsException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TableService tableService;

    private final EmployeeService employeeService;

    private final MenuService menuService;

    private final UserDetailsServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;

    private final WarehouseService warehouseService;

    private final JwtUtils jwtUtils;

    private final BranchesService branchesService;

    private final ImageService imageService;

    public AdminController(TableService tableService, EmployeeService employeeService, MenuService menuService, UserDetailsServiceImpl userServiceImpl, AuthenticationManager authenticationManager, WarehouseService warehouseService, JwtUtils jwtUtils, BranchesService branchesService, ImageService imageService) {
        this.tableService = tableService;
        this.employeeService = employeeService;
        this.menuService = menuService;
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.warehouseService = warehouseService;
        this.jwtUtils = jwtUtils;
        this.branchesService = branchesService;
        this.imageService = imageService;
    }

    @ApiOperation(value = "?????????????????????? ????????????")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserDto userDto) throws Exception {
        if (userServiceImpl.auth(userDto.getPhoneNumber())) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userDto.getPhoneNumber(),
                                userDto.getCode())
                );
            } catch (BadCredentialsException e) {
                throw new Exception("???????????????? ??????!", e);
            }
            final UserDetails userDetails = userServiceImpl.loadUserByUsername(userDto.getPhoneNumber());
            return new ResponseEntity<>(jwtUtils.generateToken(userDetails), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "???????????????????? ????????")
    @PostMapping("/menu/add")
    public ResponseEntity<MenuDTO> create(@RequestBody MenuDTO menuDTO) throws Exception {
        return new ResponseEntity<>(menuService.add(menuDTO), HttpStatus.OK);
    }

    @ApiOperation(value = "???????????????????? ?????????? ?? ????????")
    @PostMapping("/menu/image/{idMenu}")
    public void setPhoto(@RequestParam MultipartFile multipartFile,
                                           @PathVariable Long idMenu) throws IOException {
        imageService.setImage(multipartFile,idMenu);
    }

    @ApiOperation("???????????????? ????????")
    @DeleteMapping("/menu/delete/{menuId}")
    public DeletedDTO deleteMenuById(@PathVariable Long menuId) throws Exception {
        return menuService.deleteById(menuId);
    }

    @ApiOperation("???????????????????? ????????")
    @PutMapping("/menu/update")
    public ResponseEntity<MenuForAdminDTO> updateById(@RequestBody MenuUpdateDTO menuDeleteDTO) throws Exception {
        return new ResponseEntity<>(menuService.update(menuDeleteDTO), HttpStatus.OK);
    }

    @ApiOperation("???????? ????????")
    @GetMapping("/menu/{idCategory}")
    public List<MenuForAdminDTO> getAllMenu(@PathVariable Long idCategory) throws Exception {
        return menuService.allMenuForAdmin(idCategory);
    }

    @ApiOperation("???????????????????? ?????????? ?????????????????? (??????????)")
    @PostMapping("/warehouse/add")
    public ResponseEntity<Product> addNewProduct(@RequestBody Product product) throws AlreadyExistsException {
        return new ResponseEntity<>(warehouseService.addProduct(product), HttpStatus.OK);
    }

    @ApiOperation("?????????????????? ???????????? ?? ????????????????")
    @PutMapping("/warehouse/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return new ResponseEntity<>(warehouseService.update(product), HttpStatus.OK);
    }

    @ApiOperation("???????????????? ???????????????? ???? id ????????????????")
    @DeleteMapping("/warehouse/delete/{productId}")
    public DeletedDTO deleteProductById(@PathVariable Long productId) {
        return warehouseService.removeById(productId);
    }

    @ApiOperation(value = "?????? ???????????????? ???? ????????????")
    @GetMapping("/warehouse/{id}")
    public List<Product> getAllProductsWithWarehouse(@PathVariable int id) {
        return warehouseService.getAllForAdmin(id);
    }

    @ApiOperation(value = "?????????????????????????????? ????????????????")
    @GetMapping("/warehouse/ending")
    public List<Product> getAllRunningOutProducts() {
        return warehouseService.getAllEndingProducts();
    }

    @ApiOperation("???????????????????? ??????????????")
    @PostMapping("/branch/add")
    public ResponseEntity<Branches> addNewBranch(@RequestBody BranchDto branch) throws Exception {
        return new ResponseEntity<>(branchesService.addBranch(branch), HttpStatus.OK);
    }

    @ApiOperation("???????????????? ??????????????")
    @DeleteMapping("/branch/delete/{branchId}")
    public DeletedDTO branchDelete(@PathVariable Long branchId) throws Exception {
        return branchesService.deleted(branchId);
    }

    @ApiOperation("???????????????????? ????????????????")
    @PutMapping("/branch/update")
    public ResponseEntity<Branches> branchUpdate(@RequestBody BranchDto branch) throws Exception {
        return new ResponseEntity<>(branchesService.update(branch), HttpStatus.OK);
    }

    @ApiOperation("?????? ??????????????")
    @GetMapping("/branch/all")
    public List<Branches> getAllBranches(){
        return branchesService.getAll();
    }

    @ApiOperation("???????????????????? ?????????????????????? 2-Waiter, 3-Barista")
    @PostMapping("/employee/add")
    public ResponseEntity<EmployeeDto> addNewEmployee(@RequestBody EmployeeDto employeeDto) throws AlreadyExistsException {
        return new ResponseEntity<>(employeeService.registrationEmployee(employeeDto), HttpStatus.OK);
    }

    @ApiOperation("???????????????????? ???????????? Waiter'??")
    @PutMapping("/employee/waiter/add-tables")
    public void addWaiterTables(@RequestBody WaiterTableDto waiterTableDto) {
        employeeService.addWaiterTables(waiterTableDto);
    }

    @ApiOperation("???????????????? ????????????????????")
    @DeleteMapping("/employee/delete/{employeeId}")
    public DeletedDTO deletedEmployee(@PathVariable Long employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }

    @ApiOperation("?????????????????? ???????? ?????????????????????? ???? ??????????????")
    @GetMapping("/employee/get-all/{branchId}")
    public List<EmployeeForAdmin> getAllEmployee(@PathVariable Long branchId) {
        return employeeService.getAllEmployee(branchId);
    }

    @ApiOperation("???????????????????? ?????????????? ??????????????????????")
    @PutMapping("/employee/update")
    public ResponseEntity<EmployeeForAdmin> updateEmployee(@RequestBody EmployeeUpdateDTO employeeDto) {
        return new ResponseEntity<>(employeeService.updateEmployee(employeeDto), HttpStatus.OK);
    }

    @ApiOperation(value = "???????????????????? ??????????")
    @PostMapping("/table/add")
    public ResponseEntity<TableDTO> create(@RequestBody TableDTO tableDTO) throws AlreadyExistsException {
        return new ResponseEntity<>(tableService.add(tableDTO),HttpStatus.OK);
    }

    @ApiOperation(value = "?????????????????? ???????? ????????????")
    @GetMapping("/table/all")
    public List<TableDtoForAdmin> getAllTables(){
        return tableService.getAll();
    }

    @ApiOperation(value = "???????????????? ????????????")
    @DeleteMapping("/table/delete/{tableId}")
    public DeletedDTO deleteTable(@PathVariable Long tableId){
        return tableService.delete(tableId);
    }

}