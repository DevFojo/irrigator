package com.irrigator.web.service;

import com.irrigator.web.entity.SoilType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import redis.clients.jedis.Jedis;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleConfigServiceTests {
    @Mock
    private Jedis redis;

    @InjectMocks
    private ScheduleConfigService scheduleConfigService;

    @Test
    void testGetScheduleCronForSoilType_WithCachedValue_ShouldReturnCachedValue() {
        
        when(redis.get("SANDY_CRON")).thenReturn("0 0 * * *");

        
        String cron = scheduleConfigService.getScheduleCronForSoilType(SoilType.SANDY);

        
        Assertions.assertEquals("0 0 * * *", cron);
    }

    @Test
    void testGetScheduleCronForSoilType_WithoutCachedValue_ShouldReturnDefault() {
        
        when(redis.get("SANDY_CRON")).thenReturn(null);

        
        String cron = scheduleConfigService.getScheduleCronForSoilType(SoilType.SANDY);

        
        Assertions.assertEquals("*/5 * * * *", cron);
    }
}
