package com.home.monitoringservicebot.service;

import com.home.monitoringservicebot.config.TelegramBotConfig;
import com.home.monitoringservicebot.dao.MyRepository;
import com.home.monitoringservicebot.dto.ScheduledTask;
import com.home.monitoringservicebot.dto.ServiceCrash;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private TelegramBotConfig botConfig;
    private MyRepository myRepository;
    private MyScheduler myScheduler;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().replace("@" + botConfig.getBotName(), "");
            String data = "";

            if (message.contains(" ")) {
                data = message.substring(message.indexOf(" ") + 1);
                message = message.substring(0, message.indexOf(" "));
            }
            String chatId = update.getMessage().getChatId().toString();

            switch (message) {
                case "/help":
                    sendText(chatId,
                            "/getTaskList - получить текущий список задач" + "\n" +
                                    "/addTask {service_name} {api_link} {good_answer} {timeout_sec} - добавить задачу" + "\n" +
                                    "/removeTask {id} - удалить задачу" + "\n" +
                                    "/removeAllTask - удалить все задачи" + "\n" +
                                    "/getLastCrash - получить последний сбой" + "\n" +
                                    "/get10LastCrash - получить 10 последних сбоев" + "\n" +
                                    "/help - список команд"
                    );
                    break;

                case "/getTaskList":
                    getTaskList(chatId);
                    break;

                case "/addTask":
                    addTask(chatId, data);
                    break;

                case "/removeTask":
                    removeTask(chatId, data);
                    break;

                case "/removeAllTask":
                    removeAllTask(chatId);
                    break;

                case "/getLastCrash":
                    getLastCrash(chatId);
                    break;

                case "/get10LastCrash":
                    get10LastCrash(chatId);
                    break;
            }
        }
    }

    public void getTaskList(String chatId) {
        List<ScheduledTask> list = myRepository.getScheduledTaskAll();

        if (!list.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (ScheduledTask task : list) {
                message.append("id: ").append(task.getId())
                        .append(", chatId: ").append(task.getChatId())
                        .append(", service: ").append(task.getServiceName())
                        .append(", timeout: ").append(task.getTimeoutSec()).append("\n");
            }
            sendText(chatId, message.substring(0, message.length() - 1));
        } else {
            sendText(chatId, "Список пуст");
        }
    }

    public void addTask(String chatId, String data) {

        //@TODO SQL injection
        String[] dt = data.split(" ");
        String serviceName = dt[0];
        String apiLink = dt[1];
        String goodAnswer = dt[2];
        int timeoutSec = Integer.parseInt(dt[3]);

        if (myRepository.addScheduledTask(chatId, serviceName, apiLink, goodAnswer, timeoutSec)) {
            myScheduler.initScheduledTimeStamp();
            sendText(chatId, "ОК");
        } else {
            sendText(chatId, "Неверные данные");
        }

    }

    public void removeTask(String chatId, String id) {
        if (myRepository.deleteScheduledTaskById(Integer.parseInt(id))) {
            myScheduler.initScheduledTimeStamp();
            sendText(chatId, "OK");
        } else {
            sendText(chatId, "Неверные данные");
        }
    }

    public void removeAllTask(String chatId) {
        if (myRepository.deleteScheduledAllTask()) {
            myScheduler.initScheduledTimeStamp();
            sendText(chatId, "OK");
        } else {
            sendText(chatId, "Неверные данные");
        }
    }

    public void getLastCrash(String chatId) {
        ServiceCrash crash = myRepository.getLastServiceCrash();

        if (crash != null) {
            StringBuilder message = new StringBuilder();
            message.append("id: ").append(crash.getId())
                    .append(", timestamp: ").append(crash.getTimestamp())
                    .append(", service: ").append(crash.getServiceName()).append("\n");
            sendText(chatId, message.substring(0, message.length() - 1));
        } else {
            sendText(chatId, "Список пуст");
        }
    }

    public void get10LastCrash(String chatId) {
        List<ServiceCrash> list = myRepository.get10LastServiceCrash();
        if (!list.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (ServiceCrash crash : list) {
                message.append("id: ").append(crash.getId())
                        .append(", timestamp: ").append(crash.getTimestamp())
                        .append(", service: ").append(crash.getServiceName()).append("\n");

            }
            sendText(chatId, message.substring(0, message.length() - 1));
        } else {
            sendText(chatId, "Список пуст");
        }
    }

    public void sendText(String chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
