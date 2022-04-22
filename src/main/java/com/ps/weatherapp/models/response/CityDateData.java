package com.ps.weatherapp.models.response;

import lombok.Data;

import java.util.List;

@Data
public class CityDateData {

    List<Double> temperatureData;
    List<Double> windData;
}
