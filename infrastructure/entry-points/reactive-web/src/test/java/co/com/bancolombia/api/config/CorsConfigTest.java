package co.com.bancolombia.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private  CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void corsWebFilter() {

        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter("origin");
        Assertions.assertNotNull(corsWebFilter);

    }
}