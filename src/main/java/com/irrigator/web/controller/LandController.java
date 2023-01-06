package com.irrigator.web.controller;

import com.irrigator.web.dto.LandDto;
import com.irrigator.web.entity.Land;
import com.irrigator.web.model.ApiResponse;
import com.irrigator.web.service.ILandService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class LandController {

    private final ILandService landService;

    private final ModelMapper modelMapper;

    public LandController(ILandService landService, ModelMapper modelMapper) {
        this.landService = landService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/api/lands/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<?>> get(@PathVariable(value = "id") UUID id) {
        try {
            Land land = landService.getById(id);
            ApiResponse<?> responseBody;
            if (land != null) {
                responseBody = new ApiResponse<>(land);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody = new ApiResponse<>(String.format("land with id:%s not found", id));
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ApiResponse<?> responseBody = new ApiResponse<>(e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/api/lands", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<?>> list() {
        try {
            List<Land> lands = landService.list();
            ApiResponse<?> responseBody = lands != null ? new ApiResponse<>(lands) : new ApiResponse<>(List.of());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> responseBody = new ApiResponse<>(e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/api/lands", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<?>> create(@RequestBody LandDto land) {
        try {
            Land newLand = landService.create(convertToEntity(land));
            ApiResponse<?> responseBody = new ApiResponse<>(newLand);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> responseBody = new ApiResponse<>(e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/api/lands/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<?>> update(@PathVariable(value = "id") UUID id, @RequestBody LandDto land) {
        try {
            Land updatedLand = landService.update(id, convertToEntity(land));
            ApiResponse<?> responseBody;
            if (updatedLand != null) {
                responseBody = new ApiResponse<>(updatedLand);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody = new ApiResponse<>(String.format("land with id:%s not found", id));
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ApiResponse<?> responseBody = new ApiResponse<>(e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Land convertToEntity(LandDto dto) {
        return modelMapper.map(dto, Land.class);
    }
}
