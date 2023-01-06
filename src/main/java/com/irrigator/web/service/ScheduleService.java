package com.irrigator.web.service;

import com.cronutils.model.Cron;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.irrigator.web.entity.Land;
import com.irrigator.web.entity.Schedule;
import com.irrigator.web.entity.ScheduleState;
import com.irrigator.web.entity.SoilType;
import com.irrigator.web.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Component
public class ScheduleService implements IScheduleService {
    @Value("${job.retry.count}")
    private int retryCount;
    private final ScheduleRepository scheduleRepository;

    private final ScheduleConfigService scheduleConfigService;

    private final CronParser cronParser;

    public ScheduleService(ScheduleConfigService scheduleConfigService, ScheduleRepository scheduleRepository, CronParser cronParser) {
        this.scheduleConfigService = scheduleConfigService;
        this.scheduleRepository = scheduleRepository;
        this.cronParser = cronParser;
    }

    @Override
    public void createScheduleForLand(Land land) {
        Schedule schedule = new Schedule();
        schedule.setLand(land);
        schedule.setState(ScheduleState.INITIAL);
        schedule.setAttemptsLeft(retryCount);
        schedule.setRunTime(nextExecutionTime(land.getSoilType()));
        schedule.setCreateTime(Date.from(Instant.now()));
        schedule.setUpdateTime(Date.from(Instant.now()));

        scheduleRepository.save(schedule);
    }

    private Date nextExecutionTime(SoilType soilType) {
        String scheduleCron = scheduleConfigService.getScheduleCronForSoilType(soilType);
        Cron cron = cronParser.parse(scheduleCron);
        Optional<Duration> durationToNextExecution = ExecutionTime.forCron(cron).timeToNextExecution(ZonedDateTime.now());
        return durationToNextExecution.map(duration -> Date.from(Instant.now().plus(duration))).orElse(null);
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        schedule.setUpdateTime(Date.from(Instant.now()));
        scheduleRepository.saveAndFlush(schedule);
    }

    @Override
    public Schedule getById(UUID id) {
        Optional<Schedule> result = scheduleRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<Schedule> list() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getPending() {
        return scheduleRepository.findPending();
    }
}
