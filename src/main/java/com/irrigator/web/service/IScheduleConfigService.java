package com.irrigator.web.service;

import com.irrigator.web.entity.SoilType;

public interface IScheduleConfigService {
    String getScheduleCronForSoilType(SoilType soilType);
}
