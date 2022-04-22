package com.ps.weatherapp.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class CityWeatherData {

    private long dt;
    private DayTempInfo main;
    private DayWindInfo wind;
    private CityRainInfo rain;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String dt_txt;
}
