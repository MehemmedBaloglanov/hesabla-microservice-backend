package com.hesabla.invoicing;

import org.springframework.boot.SpringApplication;

public class TestInvoicingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(InvoicingServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
