package com.jab.microservices;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.*;

@SpringBootTest
class EndpointUnitTests {

    @Mock
    private Service service;

    @InjectMocks
    private Endpoint endpoint;

    @Test
    public void given_controller_when_call_convert_then_ok() {

        //When
        when(service.rate("eur", "usd"))
            .thenReturn(new BigDecimal("1.1819"));

        var amount = "100";
        var expected = "118.19";
        var result = endpoint.convert(amount);

        //Then
        then(result).isEqualTo(expected);
        verify(service, times(1)).rate("eur", "usd");
    }
}
