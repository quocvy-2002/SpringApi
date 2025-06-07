package com.example.springApi.controller;

import com.example.springApi.dto.response.WeatherResponse;
import com.example.springApi.service.WeatherService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public Mono<WeatherResponse> getWeather(@RequestParam String city) {
        return weatherService.getWeatherByCity(city);
    }
}