package com.irrigator.web.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FakeIrrigatorSensorTests {
    @InjectMocks
    private FakeIrrigatorSensor fakeIrrigatorSensor;

    @Test
    void testTrigger() {
        try {
            fakeIrrigatorSensor.trigger("123");
        } catch (Exception ex) {
            Assertions.assertEquals("error connecting to sensor", ex.getMessage());
        }
    }
}
