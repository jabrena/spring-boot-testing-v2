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

    @GetMapping("/convert/eur/usd/{amount}")
    public @ResponseBody String convert(@PathVariable("amount") String amount) {

        BigDecimal finalAmount = new BigDecimal(amount);
        BigDecimal rate = service.rate("eur", "usd");

        return finalAmount.multiply(rate).setScale(2).toString();
    }

}
