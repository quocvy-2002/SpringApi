package com.example.springApi.client;

import com.example.springApi.dto.response.WeatherResponse;
import com.example.springApi.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WeatherClient {

    private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);

    private final WebClient webClient;
    private final String apiKey;

    public WeatherClient(WebClient webClient, @Value("${weather.api.key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    public Mono<WeatherResponse> getWeather(String city) {
        logger.info("Calling OpenWeatherMap API for city: {}", city);
        return webClient.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                            .path("/weather")
                            .queryParam("q", city)
                            .queryParam("appid", apiKey)
                            .build();
                    logger.debug("Request URL: {}", uri);
                    return uri;
                })
                .retrieve()
                .onStatus(
                        status -> status instanceof HttpStatus && ((HttpStatus) status).is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("API error: {} - {}", response.statusCode(), body);
                                    return Mono.error(new ApiException("Client error: " + response.statusCode() + " - " + body));
                                }))
                .onStatus(
                        status -> status instanceof HttpStatus && ((HttpStatus) status).is5xxServerError(),
                        response -> Mono.error(new ApiException("Server error: " + response.statusCode()))
                )
                .bodyToMono(WeatherResponse.class)
                .doOnSuccess(response -> logger.info("Received response for city: {}", city))
                .switchIfEmpty(Mono.error(new ApiException("No data returned from API")));
    }
}
