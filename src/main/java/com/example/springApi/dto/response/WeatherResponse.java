package com.example.springApi.dto.response;

import com.example.springApi.entity.Main;
import lombok.Data;

@Data
public class WeatherResponse {
    private String name;
    private Main main;

}
