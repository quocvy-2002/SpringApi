package com.example.springApi.service;

import com.example.springApi.client.WeatherClient;
import com.example.springApi.dto.response.WeatherResponse;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public Mono<WeatherResponse> getWeatherByCity(String city) {
        return weatherClient.getWeather(city)
                .map(response -> {
                    if (response.getMain() != null) {
                        response.getMain().setTemp(response.getMain().getTemp() - 273.15);
                    }
                    return response;
                });
    }
}