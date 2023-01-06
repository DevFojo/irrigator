package com.irrigator.web.service;

import com.irrigator.web.entity.Land;
import com.irrigator.web.repository.LandRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Primary
@Component
public class LandService implements ILandService {

    private final LandRepository landRepository;

    private final IScheduleService scheduleService;

    public LandService(IScheduleService scheduleService, LandRepository landRepository) {
        this.scheduleService = scheduleService;
        this.landRepository = landRepository;
    }

    @Override
    public Land getById(UUID id) {
        Optional<Land> result = landRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<Land> list() {
        return landRepository.findAll();
    }

    @Override
    public Land create(Land land) {
        land.setCreateTime(Date.from(Instant.now()));
        land.setUpdateTime(Date.from(Instant.now()));
        Land newLand = landRepository.save(land);
        scheduleService.createScheduleForLand(newLand);
        return newLand;


    }

    @Override
    public Land update(UUID id, Land land) {
        Land existingLand = getById(id);
        if (existingLand == null) {
            return null;
        }
        land.setId(id);
        land.setUpdateTime(Date.from(Instant.now()));
        return landRepository.save(land);
    }
}

