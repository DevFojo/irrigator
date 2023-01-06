package com.irrigator.web.controller;

import com.irrigator.web.entity.Schedule;
import com.irrigator.web.model.ApiResponse;
import com.irrigator.web.service.IScheduleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTests {
    @Mock
    private IScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @Test
    void testGet_ScheduleFound_ShouldReturnSchedule() {
        
        UUID id = UUID.randomUUID();
        Schedule schedule = new Schedule();
        Mockito.doReturn(schedule).when(scheduleService).getById(Mockito.eq(id));

        
        ResponseEntity<ApiResponse<?>> result = scheduleController.get(id);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(schedule, response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testGet_ScheduleNotFound_ShouldReturnNotFound() {
        
        UUID id = UUID.randomUUID();
        Mockito.doReturn(null).when(scheduleService).getById(Mockito.eq(id));

        
        ResponseEntity<ApiResponse<?>> result = scheduleController.get(id);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format("schedule with id:%s not found", id), response.getError());
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void testGet_ExceptionThrown_ShouldReturnInternalServerError() {
        
        UUID id = UUID.randomUUID();
        Mockito.doThrow(new RuntimeException("error")).when(scheduleService).getById(Mockito.eq(id));

        
        ResponseEntity<ApiResponse<?>> result = scheduleController.get(id);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("error", response.getError());
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void testList_SchedulesFound_ShouldReturnSchedules() {
        
        List<Schedule> schedules = List.of(new Schedule());
        Mockito.doReturn(schedules).when(scheduleService).list();

        
        ResponseEntity<ApiResponse<?>> result = scheduleController.list();

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(schedules, response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testList_SchedulesNotFound_ShouldReturnEmptyList() {
        
        Mockito.doReturn(null).when(scheduleService).list();

        
        ResponseEntity<ApiResponse<?>> result = scheduleController.list();

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(List.of(), response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testList_ExceptionThrown_ShouldReturnInternalServerError() {
        
        Mockito.doThrow(new RuntimeException("error")).when(scheduleService).list();

        
        ResponseEntity<ApiResponse<?>> result = scheduleController.list();

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("error", response.getError());
        Assertions.assertFalse(response.isSuccess());
    }
}
