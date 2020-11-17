package com.jab.microservices;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class E2ETests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplateTest;

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

        final int delay = 500;

        //frankfurter

        wireMockServer.stubFor(get(urlEqualTo("/frankfurter/latest"))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withFixedDelay(delay)
                .withBodyFile("providers/frankfurter.json")));

        //ratesapi

        wireMockServer.stubFor(get(urlEqualTo("/ratesapi/latest"))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withFixedDelay(delay)
                .withBodyFile("providers/ratesapi.json")));

        //exchangeratesapi

        wireMockServer.stubFor(get(urlEqualTo("/exchangeratesapi/latest"))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withFixedDelay(delay)
                .withBodyFile("providers/exchangeratesapi.json")));
    }

    final int times = 5;

    @RepeatedTest(times)
   //@Test
    public void given_controller_when_call_endpoint_then_ok() {

        //Given
        loadStubs();

        //When
        var resource = "/convert/eur/usd/100";
        var address = String.format("%s%s%s", "http://localhost:", port, resource);
        var result = restTemplateTest.getForObject(address, String.class);

        //Then
        then(result).isEqualTo("118.19");
    }

    @RepeatedTest(times)
    //@Test
    public void given_controller_when_call_endpointAsync_then_ok() {

        //Given
        loadStubs();

        //When
        var resource = "/convertasync/eur/usd/100";
        var address = String.format("%s%s%s", "http://localhost:", port, resource);
        var result = restTemplateTest.getForObject(address, String.class);

        //Then
        then(result).isEqualTo("118.19");
    }

}
