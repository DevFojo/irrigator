package com.irrigator.web.repository;

import com.irrigator.web.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    @Query(value = "SELECT * FROM schedule WHERE state = 'INITIAL' AND run_time <= CURRENT_TIMESTAMP", nativeQuery = true)
    List<Schedule> findPending();
}
