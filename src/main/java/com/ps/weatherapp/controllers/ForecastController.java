package com.ps.weatherapp.controllers;

import com.ps.weatherapp.services.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @GetMapping("/forecast/{cityName}")
    public ResponseEntity<Object> forecastOf(@PathVariable @NotBlank String cityName) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]*$");
        Matcher matcher = pattern.matcher(cityName);
        if (matcher.matches()) {
            return new ResponseEntity<>(forecastService.getForecastFor(cityName), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid input, please check and try again", HttpStatus.BAD_REQUEST);
        }
    }
}
