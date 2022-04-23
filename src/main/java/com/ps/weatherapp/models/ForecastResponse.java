package com.ps.weatherapp.models;

import lombok.Data;


@Data
public class ForecastResponse {

    private String status;
    private DayTemperature dayTemperature;
}