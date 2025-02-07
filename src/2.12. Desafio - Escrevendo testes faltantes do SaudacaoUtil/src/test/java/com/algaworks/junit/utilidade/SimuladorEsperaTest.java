package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorEsperaTest {

    @Test
    //@Disabled("Não é mais aplicável")
    @EnabledIfEnvironmentVariable(named = "ENV", matches = "DEV")
    void deveEsperarENaoDarTimeout() {


//        Assertions.assertTimeout(Duration.ofSeconds(1),
//                () -> SimuladorEspera.esperar(Duration.ofSeconds(10)));

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1),
                () -> SimuladorEspera.esperar(Duration.ofMillis(10)));

        Assumptions.assumeTrue("PROD".equals(System.getenv("ENV")),
                () -> "Abordando teste: Não deve ser executado em prod");
    }
}