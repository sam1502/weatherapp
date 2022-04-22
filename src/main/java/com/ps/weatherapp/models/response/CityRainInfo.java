package com.ps.weatherapp.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CityRainInfo {
    @JsonProperty("1h")
    private String h;
}
