package com.ps.weatherapp.services;

import com.ps.weatherapp.models.response.OpenWeatherResponse;

public interface OpenWeatherMapService {

    OpenWeatherResponse getForecastData(String cityName);
}
