package com.ps.weatherapp.exceptions;

public class WeatherServiceException extends RuntimeException {

    public WeatherServiceException(String message) {
        super(message);
    }
}
