package com.example.NeoCafe.entity;

import com.example.NeoCafe.Enums.Status;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "branches")
public class Branches{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private  String name;

    @Column(name ="link_2_gis")
    private String link2gis;

    @Column(name ="phone_number")
    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    private BranchTimeTable workingTime;

}
