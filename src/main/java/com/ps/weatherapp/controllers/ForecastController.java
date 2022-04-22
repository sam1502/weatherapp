package com.ps.weatherapp.controllers;

import com.ps.weatherapp.models.ForecastResponse;
import com.ps.weatherapp.models.response.CityDateData;
import com.ps.weatherapp.services.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotBlank;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @GetMapping("/forecast/{cityName}")
    public Map<LocalDate, CityDateData> forecastOf(@PathVariable @NotBlank String cityName) {
        return forecastService.getForecastFor(cityName);
    }
}
