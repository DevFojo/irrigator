package com.irrigator.web.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class FakeIrrigatorSensor implements IIrrigatorSensor {
    @Override
    public void trigger(String landID) throws Exception {

        Random random = new Random();
        TimeUnit.SECONDS.sleep(random.nextInt(1, 20));
        if (!random.nextBoolean()) {
            throw new Exception("error connecting to sensor");
        }
    }
}
