# spring-boot-testing-v2

![Java CI](https://github.com/jabrena/spring-boot-testing-v2/workflows/Java%20CI/badge.svg)

```gherkin
Feature: EUR-USD Conversor using the best Rate
Background: The provider has an agreement with few REST API
Scenario: Consume the APIs in a Happy path scenario
    Given an amount of Euros as input
    When call endpoint GET /convert/eur/usd/{amount}
    Then ask rate for: https://api.frankfurter.app/latest
    And  ask rate for: https://api.ratesapi.io/api/latest
    And  ask rate for: https://api.exchangeratesapi.io/latest
    And  use the best rate for USD
    Then return {amount}*rate
```

# How to build

```
./mvnw clean test
```

## References:

https://cucumber.io/docs/gherkin/reference/

