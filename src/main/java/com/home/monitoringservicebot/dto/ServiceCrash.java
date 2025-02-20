package com.home.monitoringservicebot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class ServiceCrash {
    @Id
    private long id;
    private Timestamp timestamp;
    private String serviceName;
    private String error;
}
