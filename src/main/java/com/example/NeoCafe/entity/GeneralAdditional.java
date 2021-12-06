package com.example.NeoCafe.entity;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.Enums.TypeGeneralAdditional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "general_additional")
public class GeneralAdditional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Menu menu;
    
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Warehouse warehouse;

    private long quantity;

    private double price;

    @Enumerated(EnumType.STRING)
    private TypeGeneralAdditional typeGeneralAdditional;

    @Enumerated(EnumType.STRING)
    private Status status;
}
