package com.jab.microservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
public class Endpoint {

    @Autowired
    Service service;

    private String calculate(String amount, BigDecimal rate) {

        BigDecimal finalAmount = new BigDecimal(amount);
        return finalAmount.multiply(rate).setScale(2).toString();
    }

    @GetMapping("/convert/eur/usd/{amount}")
    public @ResponseBody String convert(@PathVariable("amount") String amount) {

        BigDecimal rate = service.rate("eur", "usd");

        return calculate(amount, rate);
    }

    @GetMapping("/convertasync/eur/usd/{amount}")
    public @ResponseBody String convertAsync(@PathVariable("amount") String amount) {

        BigDecimal rate = service.rateAsync("eur", "usd");

        return calculate(amount, rate);
    }

}
