package com.home.monitoringservicebot.dao;

import com.home.monitoringservicebot.dto.ScheduledTask;
import com.home.monitoringservicebot.dto.ServiceCrash;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ServiceCrashMapper implements RowMapper<ServiceCrash> {

    @Override
    public ServiceCrash mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        Timestamp timestamp = rs.getTimestamp("timestamp");
        String serviceName = rs.getString("service_name");

        return new ServiceCrash(id, timestamp, serviceName);
    }
}
