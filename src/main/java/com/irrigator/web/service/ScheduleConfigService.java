package com.irrigator.web.service;

import com.irrigator.web.entity.SoilType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Primary
@Component
public class ScheduleConfigService implements IScheduleConfigService {
    private final Jedis redis;

    private final Logger logger = LoggerFactory.getLogger(ScheduleConfigService.class);

    private final String DEFAULT_CRON = "*/5 * * * *";

    public ScheduleConfigService(Jedis redis) {
        this.redis = redis;
    }

    public String getScheduleCronForSoilType(SoilType soilType) {
        String key = getKey(soilType);
        String value = this.redis.get(key);
        if (value == null || value.isEmpty()) {
            logger.warn("Cannot find cron value for soil type: {} in cache, reverting to default", soilType);
            return DEFAULT_CRON;
        }
        return value;
    }

    private static String getKey(SoilType soilType) {
        return String.format("%s_CRON", soilType.toString().toUpperCase());
    }
}
