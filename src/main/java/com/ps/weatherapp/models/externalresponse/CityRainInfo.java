package com.ps.weatherapp.models.externalresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CityRainInfo {
    @JsonProperty("1h")
    private String h;
}
