package com.home.monitoringservicebot.service;

import com.home.monitoringservicebot.dto.ScheduledTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class SchedulerService {
    private TelegramBot bot;
    private MyScheduler myScheduler;
    private CheckIsAliveService checkIsAliveService;

    @Scheduled(fixedRate = 5 * 60000, initialDelay = 60000)
    public void checkServices() {
        log.debug("Checking services...");

        for (ScheduledTask task : myScheduler.getSchedulerTaskList()) {
            if (!checkIsAliveService.getStatus(task.getApiLink(), task.getGoodAnswer()).equals("OK")) {
                //@TODO
            }
        }
    }


}
