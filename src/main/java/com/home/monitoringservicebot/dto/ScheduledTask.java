package com.home.monitoringservicebot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Data
public class ScheduledTask {
    @Id
    private long id;
    private String chatId;
    private String serviceName;
    private String apiLink;
    private String goodAnswer;
    private int timeoutSec;
}
