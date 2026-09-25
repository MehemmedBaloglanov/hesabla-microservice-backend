package com.hesabla.invoicing.controller;

import com.hesabla.invoicing.dto.CustomerRequest;
import com.hesabla.invoicing.dto.CustomerResponse;
import com.hesabla.invoicing.security.AuthenticatedUser;
import com.hesabla.invoicing.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@AuthenticationPrincipal AuthenticatedUser user,
                                                   @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.create(user.tenantId(), request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listAll(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(customerService.listAll(user.tenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getOne(@AuthenticationPrincipal AuthenticatedUser user,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getOne(user.tenantId(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@AuthenticationPrincipal AuthenticatedUser user,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.update(user.tenantId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user,
                                       @PathVariable Long id) {
        customerService.delete(user.tenantId(), id);
        return ResponseEntity.noContent().build();
    }
}
