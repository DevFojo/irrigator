package com.irrigator.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Schedule extends BaseEntity {

    @ManyToOne()
    @JoinColumn(name = "land_id")
    @JsonBackReference
    private Land land;

    private Date runTime;

    @Enumerated(EnumType.STRING)
    private ScheduleState state;

    private int attemptsLeft;

}
