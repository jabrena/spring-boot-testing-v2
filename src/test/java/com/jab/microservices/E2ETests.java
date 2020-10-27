package com.jab.microservices;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private TestRestTemplate restTemplate;

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
    public void given_controller_when_call_endpoint_then_ok() {

        loadStubs();

        var resource = "/convert/eur/usd/100";
        var address = String.format("%s%s%s", "http://localhost:", port, resource);
        var result = restTemplate.getForObject(address, String.class);

        then(result).isEqualTo("118.19");
    }

}
