package com.ps.weatherapp.models.externalresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CityRainInfo {
    @SerializedName("3h")
    private String h;
}
