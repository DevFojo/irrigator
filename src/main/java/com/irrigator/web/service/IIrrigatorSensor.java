package com.irrigator.web.service;

public interface IIrrigatorSensor {
    void trigger(String landID) throws Exception;
}
