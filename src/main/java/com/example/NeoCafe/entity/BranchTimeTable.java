package com.example.NeoCafe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "branch_time_tables")
public class BranchTimeTable {
    //время работы филиала
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "branch_time_id")
    private long branchTimeId;

    private String monday;

    private String tuesday;

    private String wednesday;

    private String thursday;

    private String friday;

    private String saturday;

    private String sunday;

}
