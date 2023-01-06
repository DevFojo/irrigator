package com.irrigator.web;

import com.irrigator.web.data.DbSeeder;
import com.irrigator.web.service.IrrigationRunner;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.Duration;
import java.util.Objects;

@SpringBootApplication
public class IrrigatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(IrrigatorApplication.class, args);
    }

    @Autowired
    private JobScheduler jobScheduler;
    @Value("${environment}")
    public String environment;
    @Autowired
    private DbSeeder dbSeeder;

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleRecurrently() {
        jobScheduler.scheduleRecurrently(Duration.ofSeconds(15), IrrigationRunner::run);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        if (Objects.equals(environment, "dev")) {
            dbSeeder.Seed();
        }
    }
}
