package com.ps.weatherapp.services;

import com.ps.weatherapp.models.ForecastResponse;
import com.ps.weatherapp.models.externalresponse.CityDateData;

import java.time.LocalDate;
import java.util.Map;

public interface ForecastService {

    Map<LocalDate, ForecastResponse> getForecastFor(String cityName);
}
