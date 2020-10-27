package com.jab.microservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTests {

	@Test
	void contextLoads() {
	}

    private MainApplication mainApplication;

    @Test
    public void main() {
        mainApplication.main(new String[]{});
    }

}
