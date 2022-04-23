package com.ps.weatherapp.models.externalresponse;

import lombok.Data;

import java.util.List;

@Data
public class CityDateData {

    List<Double> temperatureData;
    List<Double> windData;
}
