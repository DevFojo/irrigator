package com.irrigator.web.service;

import com.irrigator.web.entity.Land;
import com.irrigator.web.entity.Schedule;

import java.util.List;
import java.util.UUID;

public interface IScheduleService {
    void createScheduleForLand(Land newLand);

    Schedule getById(UUID id);

    List<Schedule> list();

    List<Schedule> getPending();

    void updateSchedule(Schedule schedule);
}
