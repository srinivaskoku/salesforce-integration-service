package com.sacumen.demo.salesforceintegrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SalesforceIntegrationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesforceIntegrationServiceApplication.class, args);
    }

}
