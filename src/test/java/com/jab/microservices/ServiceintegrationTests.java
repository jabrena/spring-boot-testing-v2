package com.jab.microservices;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@ActiveProfiles("test")
class ServiceintegrationTests {

    @Mock
    private GlobalConfiguration globalConfiguration;

    @Autowired
    private Service service;

    WireMockServer wireMockServer;

    @BeforeEach
    public void setup () {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
    }

    @AfterEach
    public void teardown () {
        wireMockServer.stop();
    }

    private void loadStubs() {

        //frankfurter

        wireMockServer.stubFor(get(urlEqualTo("/frankfurter/latest"))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("providers/frankfurter.json")));

        //ratesapi

        wireMockServer.stubFor(get(urlEqualTo("/ratesapi/latest"))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("providers/ratesapi.json")));

        //exchangeratesapi

        wireMockServer.stubFor(get(urlEqualTo("/exchangeratesapi/latest"))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("providers/exchangeratesapi.json")));
    }

    @Test
    public void given_controller_when_call_convert_then_ok() {

        //Given
        loadStubs();

        //When
        var result = service.rate("eur", "usd");

        //Then
        var expected = "1.1819";
        then(result).isEqualTo(expected);
    }
}
