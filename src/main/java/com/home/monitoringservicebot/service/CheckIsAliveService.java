package com.home.monitoringservicebot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class CheckIsAliveService {
    public String getStatus(String apiLink, String goodAnswer) {

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiLink)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body().equals(goodAnswer) ? "OK" : "ERROR";
            } else {
                return response.statusCode() + "";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return "Error";
  }
}
