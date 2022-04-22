package com.ps.weatherapp.services;

import com.ps.weatherapp.models.ForecastResponse;
import com.ps.weatherapp.models.response.CityDateData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ForecastService {

    Map<LocalDate, CityDateData> getForecastFor(String cityName);
}
