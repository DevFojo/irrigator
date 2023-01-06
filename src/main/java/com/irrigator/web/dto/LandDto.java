package com.irrigator.web.dto;

import com.irrigator.web.entity.SoilType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LandDto  {
    public String id;
    public float size;
    public SoilType soilType;
    public Date lastIrrigationTime;
}
