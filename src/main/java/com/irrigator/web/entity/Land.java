package com.irrigator.web.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Land extends BaseEntity {

    public float size;

    @Enumerated(EnumType.STRING)
    public SoilType soilType;

    public Date lastIrrigationTime;

    @OneToMany(mappedBy="land")
    @JsonManagedReference
    public List<Schedule> schedules;
}

