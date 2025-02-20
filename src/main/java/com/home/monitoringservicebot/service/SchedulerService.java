package com.home.monitoringservicebot.service;

import com.home.monitoringservicebot.dao.MyRepository;
import com.home.monitoringservicebot.dto.ScheduledTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@AllArgsConstructor
public class SchedulerService {
    private MyRepository myRepository;
    private TelegramBot telegramBot;
    private MyScheduler myScheduler;
    private CheckIsAliveService checkIsAliveService;

    @Scheduled(fixedRate = 5 * 60000, initialDelay = 20000)
    public void checkServices() {
        log.debug("Checking services...");

        for (ScheduledTask task : myScheduler.getSchedulerTaskList()) {
            String status = checkIsAliveService.getStatus(task.getApiLink(), task.getGoodAnswer());
            if (!status.equals("OK")) {
                myRepository.addCrash(task.getServiceName(),status);
                telegramBot.sendText(task.getChatId(), new Date() + " " + task.getServiceName() + " " + status);
            }
        }
    }


}
