package com.lenoox.businessbank.payments;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentsController {


    @GetMapping
    public String getAllPayments() {
        return "get getAllPayments";
    }

    @PostMapping
    public String payForUser() {
        return "get payForUser";
    }
}
