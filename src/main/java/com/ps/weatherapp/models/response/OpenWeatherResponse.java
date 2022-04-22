package com.ps.weatherapp.models.response;


import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherResponse {

    private String cod;
    private int message;
    private int cnt;
    List<CityWeatherData> list;
    private City city;
}
