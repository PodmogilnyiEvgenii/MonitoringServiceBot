package com.home.monitoringservicebot.dao;

import com.home.monitoringservicebot.dto.ScheduledTask;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduledTaskMapper implements RowMapper<ScheduledTask> {

    @Override
    public ScheduledTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String chatId = rs.getString("chat_id");
        String serviceName = rs.getString("service_name");
        String apiLink = rs.getString("api_link");
        String goodAnswer = rs.getString("good_answer");
        int timeoutSec = rs.getInt("timeout_sec");

        return new ScheduledTask(id, chatId, serviceName, apiLink, goodAnswer, timeoutSec);
    }
}
