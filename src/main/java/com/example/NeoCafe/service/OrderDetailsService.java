package com.example.NeoCafe.service;

import com.example.NeoCafe.Enums.OrderTypes;
import com.example.NeoCafe.dto.OrderById;
import com.example.NeoCafe.dto.OrderDetailsForWaiterDto;
import com.example.NeoCafe.entity.*;
import com.example.NeoCafe.exception.ResourceNotFoundException;
import com.example.NeoCafe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailsService {

    @Autowired
    private OrderDetailsRepository detailsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private GeneralAdditionalRepository generalAdditionalRepository;

    @Autowired
    private AdditionalsRepository additionalsRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public List<OrderDetails> getAll() {
        return detailsRepository.findAll();
    }

    @Transactional
    public  List<OrderDetailsForWaiterDto> allOrderDetailsByOrders(long orderId, OrderTypes orderTypes) {

        List<OrderDetails> list = detailsRepository.findAllByOrdersAndOrders_OrderType(ordersRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("нет заказа с таким id = ",orderId)),orderTypes);
        List<OrderDetailsForWaiterDto> result = new ArrayList<>();

        for(OrderDetails orderDetails: list){
            OrderDetailsForWaiterDto model = new OrderDetailsForWaiterDto();
            model.setOrderDetailsId(orderDetails.getId());
            model.setNameMenu(orderDetails.getMenu().getName());
            model.setMenuPrice(orderDetails.getMenu().getPrice());
            model.setTotalOrderDetailsPrice(orderDetails.getCalcTotal());

            List<Additionals> additionalsList = additionalsRepository.findAllByOrderDetail(orderDetails);

            List<GeneralAdditional> generalAdditionalList = new ArrayList<>();

            for (Additionals additional: additionalsList) {
                generalAdditionalList.add(generalAdditionalRepository.getById(additional.getGeneralAdditional().getId()));
            }

            List<String> additionalsName= new ArrayList<>();

            for (GeneralAdditional g:generalAdditionalList ) {
                additionalsName.add(warehouseRepository.getById(g.getWarehouse().getProductId()).getProductName());
            }

            model.setCompositions(additionalsName);
            model.setQuantity(orderDetails.getQuantity());
            result.add(model);
        }
        return result;
    }
    @Transactional
    public OrderById getAllDetailsByTable(Long tableId){
        User user  = userDetailsService.getCurrentUser();

        Orders orders = ordersRepository.findByTable_IdAndEmployee_Id(tableId,user.getId());

        OrderById orderById = new OrderById();

        orderById.setOrderStatus(orders.getOrderStatus());
        orderById.setDateOfOrder(orders.getOrderTime());
        orderById.setTableId(tableId);

        orderById.setTotalPrice(orders.getTotalPrice());

        orderById.setOrderId(orders.getId());

        List<OrderDetails> orderDetailsList = detailsRepository.findAllByOrders(orders);

        List<OrderDetailsForWaiterDto> result = new ArrayList<>();

        orderById.setOrderDetailsForWaiterDto(result);

        for(OrderDetails orderDetails: orderDetailsList){
            OrderDetailsForWaiterDto model = new OrderDetailsForWaiterDto();
            model.setOrderDetailsId(orderDetails.getId());
            model.setCategory(orderDetails.getMenu().getCategory().getId());
            model.setNameMenu(orderDetails.getMenu().getName());
            model.setMenuPrice(orderDetails.getMenu().getPrice());
            model.setTotalOrderDetailsPrice(orderDetails.getCalcTotal());

            List<Additionals> additionalsList = additionalsRepository.findAllByOrderDetail(orderDetails);

            List<GeneralAdditional> generalAdditionalList = new ArrayList<>();

            for (Additionals additional: additionalsList) {
                generalAdditionalList.add(generalAdditionalRepository.getById(additional.getGeneralAdditional().getId()));
            }

            List<String> additionalsName= new ArrayList<>();

            for (GeneralAdditional g:generalAdditionalList ) {
                additionalsName.add(warehouseRepository.getById(g.getWarehouse().getProductId()).getProductName());
            }

            model.setCompositions(additionalsName);
            model.setQuantity(orderDetails.getQuantity());
            result.add(model);
        }
        return orderById;
    }
}
