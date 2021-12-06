package com.example.NeoCafe.entity;

import com.example.NeoCafe.Enums.OrderStatus;
import com.example.NeoCafe.Enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "order_time")
    private Date orderTime;

    @Column(name = "type_order")
    @Enumerated(EnumType.STRING)
    private OrderTypes orderType;

    @ManyToOne
    private Tables table;

    @ManyToOne
    private Branches branch;

    @ManyToOne
    private User employee;

    @ManyToOne
    private User client;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}

