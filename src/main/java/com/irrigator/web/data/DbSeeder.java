package com.irrigator.web.data;

import com.irrigator.web.entity.Land;
import com.irrigator.web.entity.Schedule;
import com.irrigator.web.entity.ScheduleState;
import com.irrigator.web.entity.SoilType;
import com.irrigator.web.repository.LandRepository;
import com.irrigator.web.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbSeeder {

    @Autowired
    private LandRepository landRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public void Seed() {
        List<Land> lands = landRepository.findAll();
        if (lands.isEmpty()) {
            Land land1 = new Land();
            land1.setSoilType(SoilType.SANDY);
            land1.setSize(20.5f);
            land1.setCreateTime(Date.from(Instant.now()));
            land1.setUpdateTime(Date.from(Instant.now()));

            Land land2 = new Land();
            land2.setSoilType(SoilType.CLAY);
            land2.setSize(13.4f);
            land2.setCreateTime(Date.from(Instant.now()));
            land2.setUpdateTime(Date.from(Instant.now()));

            Land land3 = new Land();
            land3.setSoilType(SoilType.LOAM);
            land3.setSize(32.6f);
            land3.setCreateTime(Date.from(Instant.now()));
            land3.setUpdateTime(Date.from(Instant.now()));

            lands = List.of(land1, land2, land3);
            landRepository.saveAllAndFlush(lands);

        }
        List<Schedule> schedules = scheduleRepository.findAll();
        if (!lands.isEmpty() && schedules.isEmpty()) {
            schedules = lands.stream().map(land -> {
                Schedule schedule = new Schedule();
                schedule.setLand(land);
                schedule.setState(ScheduleState.INITIAL);
                schedule.setAttemptsLeft(3);
                schedule.setRunTime(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
                schedule.setCreateTime(Date.from(Instant.now()));
                schedule.setUpdateTime(Date.from(Instant.now()));
                return schedule;
            }).collect(Collectors.toList());
            scheduleRepository.saveAllAndFlush(schedules);
        }
    }
}
