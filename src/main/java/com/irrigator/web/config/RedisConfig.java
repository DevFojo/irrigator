package com.irrigator.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {
    @Value("${redis.connection-string}")
    private String connectionString;

    @Bean
    Jedis redisClient() {
        JedisPool pool = new JedisPool(this.connectionString);
        return pool.getResource();
    }
}

