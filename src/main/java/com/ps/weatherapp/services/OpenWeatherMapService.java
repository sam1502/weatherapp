package com.ps.weatherapp.services;

import com.ps.weatherapp.models.externalresponse.OpenWeatherResponse;

public interface OpenWeatherMapService {

    OpenWeatherResponse getForecastData(String cityName);
}
