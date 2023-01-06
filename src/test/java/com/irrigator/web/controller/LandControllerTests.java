package com.irrigator.web.controller;

import com.irrigator.web.dto.LandDto;
import com.irrigator.web.entity.Land;
import com.irrigator.web.model.ApiResponse;
import com.irrigator.web.service.ILandService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class LandControllerTests {

    @Mock
    private ILandService landService;

    @Mock
    private ModelMapper modelMapper;

    private LandController landController;

    @BeforeEach
    public void setUp() {
        landController = new LandController(landService, modelMapper);
    }

    @Test
    void testGet_LandFound_ShouldReturnLand() {
        
        UUID id = UUID.randomUUID();
        Land land = new Land();
        Mockito.doReturn(land).when(landService).getById(Mockito.eq(id));

        
        ResponseEntity<ApiResponse<?>> result = landController.get(id);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(land, response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testGet_LandNotFound_ShouldReturnNotFound() {
        
        UUID id = UUID.randomUUID();
        Mockito.doReturn(null).when(landService).getById(Mockito.eq(id));

        
        ResponseEntity<ApiResponse<?>> result = landController.get(id);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format("land with id:%s not found", id), response.getError());
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void testGet_ExceptionThrown_ShouldReturnInternalServerError() {
        
        UUID id = UUID.randomUUID();
        Mockito.doThrow(new RuntimeException("error")).when(landService).getById(Mockito.eq(id));

        
        ResponseEntity<ApiResponse<?>> result = landController.get(id);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("error", response.getError());
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void testList_ShouldReturnListOfLands() {
        
        Land land1 = new Land();
        Land land2 = new Land();
        Mockito.doReturn(List.of(land1, land2)).when(landService).list();

        
        ResponseEntity<ApiResponse<?>> result = landController.list();

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(List.of(land1, land2), response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testList_NoLandsFound_ShouldReturnEmptyList() {
        
        Mockito.doReturn(null).when(landService).list();

        
        ResponseEntity<ApiResponse<?>> result = landController.list();

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(List.of(), response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testList_ExceptionThrown_ShouldReturnInternalServerError() {
        
        Mockito.doThrow(new RuntimeException("error")).when(landService).list();

        
        ResponseEntity<ApiResponse<?>> result = landController.list();

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("error", response.getError());
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void testCreate_ShouldReturnCreatedLand() {
        
        LandDto landDto = new LandDto();
        Land land = new Land();
        Mockito.doReturn(land).when(landService).create(Mockito.any(Land.class));
        Mockito.doReturn(land).when(modelMapper).map(Mockito.eq(landDto), Mockito.eq(Land.class));

        
        ResponseEntity<ApiResponse<?>> result = landController.create(landDto);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(land, response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testCreate_ExceptionThrown_ShouldReturnInternalServerError() {
        
        LandDto landDto = new LandDto();
        Mockito.doReturn(new Land()).when(modelMapper).map(landDto, Land.class);
        Mockito.doThrow(new RuntimeException("error")).when(landService).create(Mockito.any(Land.class));

        
        ResponseEntity<ApiResponse<?>> result = landController.create(landDto);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("error", response.getError());
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void testUpdate_LandFound_ShouldReturnUpdatedLand() {
        
        UUID id = UUID.randomUUID();
        LandDto landDto = new LandDto();
        Land land = new Land();
        Mockito.doReturn(land).when(landService).update(Mockito.eq(id), Mockito.any(Land.class));
        Mockito.doReturn(land).when(modelMapper).map(Mockito.eq(landDto), Mockito.eq(Land.class));

        
        ResponseEntity<ApiResponse<?>> result = landController.update(id, landDto);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(land, response.getData());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testUpdate_LandNotFound_ShouldReturnNotFound() {
        
        UUID id = UUID.randomUUID();
        LandDto landDto = new LandDto();
        Mockito.doReturn(new Land()).when(modelMapper).map(landDto, Land.class);
        Mockito.doReturn(null).when(landService).update(Mockito.eq(id), Mockito.any(Land.class));

        
        ResponseEntity<ApiResponse<?>> result = landController.update(id, landDto);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format("land with id:%s not found", id), response.getError());
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void testUpdate_ExceptionThrown_ShouldReturnInternalServerError() {
        
        UUID id = UUID.randomUUID();
        LandDto landDto = new LandDto();
        Mockito.doReturn(new Land()).when(modelMapper).map(landDto, Land.class);
        Mockito.doThrow(new RuntimeException("error")).when(landService).update(Mockito.eq(id), Mockito.any(Land.class));

        
        ResponseEntity<ApiResponse<?>> result = landController.update(id, landDto);

        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        ApiResponse<?> response = result.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("error", response.getError());
        Assertions.assertFalse(response.isSuccess());
    }
}
