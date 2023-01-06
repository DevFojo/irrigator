package com.irrigator.web.service;

import com.irrigator.web.entity.Land;
import com.irrigator.web.entity.Schedule;
import com.irrigator.web.entity.ScheduleState;
import lombok.SneakyThrows;
import org.jobrunr.jobs.lambdas.JobLambda;
import org.jobrunr.scheduling.JobScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class IrrigationRunnerTests {

    @Mock
    private JobScheduler jobScheduler;

    @Mock
    private IScheduleService scheduleService;

    @Mock
    private IIrrigatorSensor irrigatorSensor;

    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private IrrigationRunner irrigationRunner;

    @SneakyThrows
    @Test
    void testRun_withPendingSchedules() {
        Schedule schedule1 = new Schedule();
        schedule1.setId(UUID.randomUUID());
        schedule1.setAttemptsLeft(3);
        Schedule schedule2 = new Schedule();
        schedule2.setId(UUID.randomUUID());
        schedule2.setAttemptsLeft(2);
        List<Schedule> pendingSchedules = Arrays.asList(schedule1, schedule2);

        Mockito.when(scheduleService.getPending()).thenReturn(pendingSchedules);

        irrigationRunner.run();

        Mockito.verify(jobScheduler, Mockito.times(2)).enqueue(Mockito.<JobLambda>any());
    }

    @SneakyThrows
    @Test
    void testRun_withEmptyPendingSchedules() {
        Mockito.when(scheduleService.getPending()).thenReturn(Collections.emptyList());

        irrigationRunner.run();

        Mockito.verify(jobScheduler, Mockito.never()).enqueue(Mockito.<JobLambda>any());
        Mockito.verify(scheduleService, Mockito.never()).getById(Mockito.any(UUID.class));
        Mockito.verify(irrigatorSensor, Mockito.never()).trigger(Mockito.anyString());
        Mockito.verify(scheduleService, Mockito.never()).updateSchedule(Mockito.any(Schedule.class));
        Mockito.verify(notificationService, Mockito.never()).notify(Mockito.anyString());
    }

    @SneakyThrows
    @Test
    void testInitiateIrrigation_success() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setAttemptsLeft(3);
        schedule.setState(ScheduleState.PROCESSING);
        schedule.setLand(new Land());
        String landId = UUID.randomUUID().toString();
        schedule.getLand().setId(UUID.fromString(landId));

        Mockito.when(scheduleService.getById(scheduleId)).thenReturn(schedule);

        irrigationRunner.initiateIrrigation(scheduleId);

        Mockito.verify(scheduleService).getById(scheduleId);
        Mockito.verify(irrigatorSensor).trigger(landId);
        Mockito.verify(scheduleService, Mockito.times(2)).updateSchedule(schedule);
        Mockito.verify(notificationService, Mockito.never()).notify(Mockito.anyString());
    }

    @SneakyThrows
    @Test
    void testInitiateIrrigation_failure() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setAttemptsLeft(3);
        schedule.setState(ScheduleState.PROCESSING);
        schedule.setLand(new Land());
        String landId = UUID.randomUUID().toString();
        schedule.getLand().setId(UUID.fromString(landId));

        Mockito.when(scheduleService.getById(scheduleId)).thenReturn(schedule);
        Mockito.doThrow(new RuntimeException("Error occurred while triggering sensor")).when(irrigatorSensor).trigger(landId);

        irrigationRunner.initiateIrrigation(scheduleId);

        Mockito.verify(scheduleService).getById(scheduleId);
        Mockito.verify(irrigatorSensor).trigger(landId);
        Mockito.verify(scheduleService, Mockito.times(2)).updateSchedule(schedule);
        Mockito.verify(jobScheduler).schedule(Mockito.any(Instant.class), Mockito.<JobLambda>any());
        Mockito.verify(notificationService, Mockito.never()).notify(Mockito.anyString());
    }

    @SneakyThrows
    @Test
    void testInitiateIrrigation_failureWithExhaustedAttempts() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setAttemptsLeft(0);
        schedule.setState(ScheduleState.PROCESSING);
        schedule.setLand(new Land());
        String landId = UUID.randomUUID().toString();
        schedule.getLand().setId(UUID.fromString(landId));

        Mockito.when(scheduleService.getById(scheduleId)).thenReturn(schedule);
        Mockito.doThrow(new RuntimeException("Error occurred while triggering sensor")).when(irrigatorSensor).trigger(landId);

        irrigationRunner.initiateIrrigation(scheduleId);

        Mockito.verify(scheduleService).getById(scheduleId);
        Mockito.verify(irrigatorSensor).trigger(landId);
        Mockito.verify(scheduleService, Mockito.times(2)).updateSchedule(schedule);
        Mockito.verify(jobScheduler, Mockito.never()).schedule(Mockito.any(Instant.class), Mockito.<JobLambda>any());
        Mockito.verify(notificationService).notify(String.format("Irrigation failed for land: %s", landId));
    }
}
