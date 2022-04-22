package com.ps.weatherapp.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ps.weatherapp.models.response.OpenWeatherResponse;
import com.ps.weatherapp.services.OpenWeatherMapService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
public class OpenWeatherMapServiceImpl implements OpenWeatherMapService {

    @Autowired
    private RestTemplate restTemplate;

    private Gson json = new Gson();

    @Override
    public OpenWeatherResponse getForecastData(String cityName) {
        OpenWeatherResponse openWeatherResponses = new OpenWeatherResponse();
        String url = "https://api.openweathermap.org/data/2.5/forecast/";
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("q", cityName)
                    .queryParam("units", "metric")
                    .queryParam("appid", "d2929e9483efc82c82c32ee7e02d563e");

            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    new HttpEntity<>(httpHeaders), String.class);

            openWeatherResponses = json.fromJson(response.getBody(), OpenWeatherResponse.class);
        } catch (JsonSyntaxException e) {
            log.error("Exception caught while deserializing response : ", e);
        }
        return openWeatherResponses;
    }
}