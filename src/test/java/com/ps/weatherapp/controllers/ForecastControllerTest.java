package com.ps.weatherapp.controllers;


import com.ps.weatherapp.services.ForecastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ForecastControllerTest {

    @Spy
    @InjectMocks
    ForecastController forecastController;

    @Mock
    ForecastService forecastService;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(forecastController).build();
    }

    @Test
    @DisplayName("When city name is available")
    public void testForecastOf() throws Exception {
        when(forecastService.getForecastFor(anyString())).thenReturn(new HashMap<>());

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/forecast/{cityName}", "Indore"))
                .andExpect(status().isOk());

        verify(forecastService, times(1)).getForecastFor(anyString());
    }
}