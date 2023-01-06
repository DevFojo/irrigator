package com.irrigator.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class FakeNotificationServiceTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private FakeNotificationService notificationService;

    @Test
    void testNotify() {
        String message = "This is a notification message";

        notificationService.notify(message);

        Mockito.verify(logger).info("!NOTIFICATION: {}", message);
    }
}





