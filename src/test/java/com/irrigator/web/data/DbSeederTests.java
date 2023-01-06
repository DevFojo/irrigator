package com.irrigator.web.data;

import com.irrigator.web.entity.Land;
import com.irrigator.web.entity.Schedule;
import com.irrigator.web.repository.LandRepository;
import com.irrigator.web.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DbSeederTests {
    @Mock
    private LandRepository landRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private DbSeeder dbSeeder;

    @Test
    void testSeed_EmptyDb_ShouldCreateLandsAndSchedules() {
        
        Mockito.doReturn(List.of()).when(landRepository).findAll();
        Mockito.doReturn(List.of()).when(scheduleRepository).findAll();

        
        dbSeeder.Seed();

        
        Mockito.verify(landRepository, Mockito.times(1)).saveAllAndFlush(Mockito.any());
        Mockito.verify(scheduleRepository, Mockito.times(1)).saveAllAndFlush(Mockito.any());
    }

    @Test
    void testSeed_LandsExist_ShouldNotCreateLands() {
        
        Land land = new Land();
        Schedule schedule = new Schedule();
        Mockito.doReturn(List.of(land)).when(landRepository).findAll();
        Mockito.doReturn(List.of(schedule)).when(scheduleRepository).findAll();

        
        dbSeeder.Seed();

        
        Mockito.verify(landRepository, Mockito.never()).saveAllAndFlush(Mockito.any());
        Mockito.verify(scheduleRepository, Mockito.never()).saveAllAndFlush(Mockito.any());
    }
}
