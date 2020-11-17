package com.jab.microservices;

import java.math.BigDecimal;

public interface Service {

    BigDecimal rate(String from, String to);
    BigDecimal rateAsync(String from, String to);

}
