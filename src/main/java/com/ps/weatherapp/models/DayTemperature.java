package com.ps.weatherapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DayTemperature {

    private double minimumTemperature;
    private double maximumTemperature;
}
