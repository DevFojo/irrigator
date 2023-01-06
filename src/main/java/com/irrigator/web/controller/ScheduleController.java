package com.irrigator.web.controller;

import com.irrigator.web.entity.Schedule;
import com.irrigator.web.model.ApiResponse;
import com.irrigator.web.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ScheduleController {

    @Autowired
    private IScheduleService scheduleService;

    @GetMapping(value = "/api/schedules/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<?>> get(@PathVariable(value = "id") UUID id) {
        try {
            Schedule schedule = scheduleService.getById(id);
            ApiResponse<?> responseBody;
            if (schedule != null) {
                responseBody = new ApiResponse<>(schedule);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody = new ApiResponse<>(String.format("schedule with id:%s not found", id));
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ApiResponse<?> responseBody = new ApiResponse<>(e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/api/schedules", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<?>> list() {
        try {
            List<Schedule> schedules = scheduleService.list();
            ApiResponse<?> responseBody = schedules != null ? new ApiResponse<>(schedules) : new ApiResponse<>(List.of());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> responseBody = new ApiResponse<>(e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
