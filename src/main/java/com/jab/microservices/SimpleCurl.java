package com.jab.microservices;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Slf4j
public class SimpleCurl {

    /**
     * Function to fetch information from an URL.
     *
     */
    //TODO Review Webclient
    static Function1<URL, Optional<String>> fetch = url -> Try.of(() -> {

        LOGGER.debug("Thread: {}", Thread.currentThread().getName());
        LOGGER.debug("Requested URL: {}", url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url.toURI())
                .build();

        return Option.some(client
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body()).toJavaOptional();
    })
    .onFailure(ex -> LOGGER.error("SimpleCURL Error: {}", ex.getLocalizedMessage(), ex))
    .recover(ex -> Optional.empty())
    .get();

}
