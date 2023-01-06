package com.irrigator.web.service;

import com.irrigator.web.entity.Schedule;
import com.irrigator.web.entity.ScheduleState;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class IrrigationRunner {

    private final JobScheduler jobScheduler;

    private final IScheduleService scheduleService;

    private final IIrrigatorSensor irrigatorSensor;

    private final INotificationService notificationService;

    @Value("${job.retry.wait-time}")
    private int retryWaitTime;

    private final Logger logger = LoggerFactory.getLogger(IrrigationRunner.class);

    public IrrigationRunner(JobScheduler jobScheduler, IScheduleService scheduleService, IIrrigatorSensor irrigatorSensor, INotificationService notificationService) {
        this.jobScheduler = jobScheduler;
        this.scheduleService = scheduleService;
        this.irrigatorSensor = irrigatorSensor;
        this.notificationService = notificationService;
    }

    public void run() {
        logger.info("Checking for pending schedules");
        List<Schedule> pendingSchedules = scheduleService.getPending();
        for (Schedule schedule : pendingSchedules) {
            logger.info("Initiating irrigation for schedule");
            UUID scheduleID = schedule.getId();
            jobScheduler.enqueue(() -> initiateIrrigation(scheduleID));
        }
    }

    @Job(retries = 0)
    public void initiateIrrigation(UUID scheduleId) {
        Schedule schedule = scheduleService.getById(scheduleId);
        int attemptsLeft = schedule.getAttemptsLeft();

        schedule.setAttemptsLeft(attemptsLeft - 1);
        schedule.setState(ScheduleState.PROCESSING);
        schedule.setRunTime(Date.from(Instant.now()));
        scheduleService.updateSchedule(schedule);

        UUID landId = schedule.getLand().getId();
        try {
            irrigatorSensor.trigger(landId.toString());
            logger.info("Successfully triggered sensor for land: {}", landId);

            schedule.setAttemptsLeft(0);
            schedule.setState(ScheduleState.SUCCESSFUL);
            scheduleService.updateSchedule(schedule);
        } catch (Exception e) {
            schedule.setState(ScheduleState.FAILED);
            scheduleService.updateSchedule(schedule);
            logger.error("Error occurred while triggering sensor for land: {}", landId, e);
            if (attemptsLeft > 0) {
                jobScheduler.schedule(Instant.now().plus(retryWaitTime, ChronoUnit.MINUTES), () -> initiateIrrigation(scheduleId));
            } else {
                notificationService.notify(String.format("Irrigation failed for land: %s", landId));
            }
        }
    }
}
