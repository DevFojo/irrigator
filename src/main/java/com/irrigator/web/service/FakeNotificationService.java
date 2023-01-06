package com.irrigator.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FakeNotificationService implements INotificationService {
    Logger logger = LoggerFactory.getLogger(FakeNotificationService.class);

    @Override
    public void notify(String message) {
        logger.info("!NOTIFICATION: {}", message);
    }
}
