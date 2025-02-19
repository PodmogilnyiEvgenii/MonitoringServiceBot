package com.home.monitoringservicebot.service;

import com.home.monitoringservicebot.dao.MyRepository;
import com.home.monitoringservicebot.dto.ScheduledTask;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MyScheduler {
    private MyRepository myRepository;
    @Getter
    private List<ScheduledTask> schedulerTaskList;

    @PostConstruct
    public void initScheduledTimeStamp() {
        schedulerTaskList = myRepository.getScheduledTaskAll();
        log.info("Scheduled tasks size: {}", schedulerTaskList.size());
    }

}
