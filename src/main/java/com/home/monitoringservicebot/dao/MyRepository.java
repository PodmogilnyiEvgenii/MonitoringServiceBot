package com.home.monitoringservicebot.dao;

import com.home.monitoringservicebot.dto.ScheduledTask;
import com.home.monitoringservicebot.dto.ServiceCrash;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
@Slf4j
public class MyRepository {
    private final NamedParameterJdbcTemplate jdbc;

    @Transactional
    public List<ScheduledTask> getScheduledTaskAll() {
        try {
            return jdbc.query("SELECT * FROM monitoring.scheduler", new ScheduledTaskMapper());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Transactional
    public List<ScheduledTask> getScheduledTaskByChatId(String chatId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            return jdbc.query("SELECT * FROM monitoring.scheduler WHERE chat_id=:chat_id", params, new ScheduledTaskMapper());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Transactional
    public boolean addScheduledTask(String chatId, String serviceName, String apiLink, String goodAnswer, int timeoutSec) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            params.put("service_name", serviceName);
            params.put("api_link", apiLink);
            params.put("good_answer", goodAnswer);
            params.put("timeout_sec", timeoutSec);

            SqlParameterSource paramSource = new MapSqlParameterSource(params);
            GeneratedKeyHolder holder = new GeneratedKeyHolder();

            jdbc.update("INSERT INTO monitoring.scheduler (chat_id, service_name, api_link, good_answer, timeout_sec) values (:chat_id, :service_name, :api_link, :good_answer, :timeout_sec)", paramSource, holder);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Transactional
    public boolean deleteScheduledTaskById(String id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            jdbc.update("DELETE FROM monitoring.scheduler WHERE id=:id", params);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Transactional
    public boolean deleteScheduledAllTask() {
        try {
            Map<String, Object> params = new HashMap<>();
            jdbc.update("DELETE FROM monitoring.scheduler", params);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Transactional
    public ServiceCrash getLastServiceCrash() {
        try {
            Map<String, Object> params = new HashMap<>();
            return jdbc.query("SELECT * FROM monitoring.crash ORDER BY timestamp DESC LIMIT 1", params, new ServiceCrashMapper()).getFirst();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public List<ServiceCrash> get10LastServiceCrash() {
        try {
            Map<String, Object> params = new HashMap<>();
            return jdbc.query("SELECT * FROM monitoring.crash ORDER BY timestamp DESC LIMIT 10", params, new ServiceCrashMapper());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }




}
