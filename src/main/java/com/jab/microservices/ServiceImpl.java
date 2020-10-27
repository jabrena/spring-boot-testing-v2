package com.jab.microservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jab.microservices.model.exchangeratesapi.Exchangeratesapi;
import com.jab.microservices.model.frankfurter.Frankfurter;
import com.jab.microservices.model.ratesapi.Ratesapi;
import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class ServiceImpl implements Service {

    @Autowired
    GlobalConfiguration providerConfiguration;

    Function<String, URL> toURL = address -> Try.of(() ->
        new URL(address)).getOrElseThrow(ex -> {
        LOGGER.error(ex.getLocalizedMessage(), ex);
        throw new RuntimeException("Bad address", ex);
    });

    Function2<String, Optional<String>, BigDecimal> deserialize = (type, response) -> {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            //TODO Refactor
            if (response.isPresent()) {

                if (type.equals("frankfurter")) {
                    Frankfurter deserializedData = objectMapper
                        .readValue(response.get(), new TypeReference<Frankfurter>() {
                        });

                    return new BigDecimal(deserializedData.getRates().getUSD())
                        .setScale(4, BigDecimal.ROUND_UP);
                } else if(type.equals("ratesapi")){
                    Ratesapi deserializedData = objectMapper
                        .readValue(response.get(), new TypeReference<Ratesapi>() {
                        });

                    return new BigDecimal(deserializedData.getRates().getUSD())
                        .setScale(4, BigDecimal.ROUND_UP);

                } else if(type.equals("exchangeratesapi")){
                    Exchangeratesapi deserializedData = objectMapper
                        .readValue(response.get(), new TypeReference<Exchangeratesapi>() {
                        });

                    return new BigDecimal(deserializedData.getRates().getUSD())
                        .setScale(4, BigDecimal.ROUND_UP);
                }

                throw new RuntimeException("No found type");
            } else {
                LOGGER.error("It is not possible to deserialize, empty object.");
                throw new RuntimeException();
            }

        } catch (IOException e) {
            LOGGER.error("Problems with deserialization");
            throw new RuntimeException(e);
        }
    };


    Function<GlobalConfiguration.Host, BigDecimal> convert = host -> {
        return toURL
                .andThen(SimpleCurl.fetch)
                .andThen(x -> {
                    LOGGER.info("{}", x.get());
                    return x;
                })
                .andThen(s -> deserialize.apply(host.getName(), s))
                .apply(host.getAddress());
    };


    @Override
    public BigDecimal rate(String from, String to) {

        return providerConfiguration.getHosts().stream()
            .map(convert)
            .sorted((i1, i2) -> i1.compareTo(i2))
            .collect(toList())
            .get(0);
    }
}
