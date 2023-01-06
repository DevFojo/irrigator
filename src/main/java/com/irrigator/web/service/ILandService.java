package com.irrigator.web.service;

import com.irrigator.web.entity.Land;

import java.util.List;
import java.util.UUID;

public interface ILandService {
    Land getById(UUID id);

    List<Land> list();

    Land create(Land land);

    Land update(UUID id, Land land);
}
