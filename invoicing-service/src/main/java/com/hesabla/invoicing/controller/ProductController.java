package com.hesabla.invoicing.controller;

import com.hesabla.invoicing.dto.ProductRequest;
import com.hesabla.invoicing.dto.ProductResponse;
import com.hesabla.invoicing.security.AuthenticatedUser;
import com.hesabla.invoicing.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@AuthenticationPrincipal AuthenticatedUser user,
                                                  @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.create(user.tenantId(), request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAll(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(productService.listAll(user.tenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getOne(@AuthenticationPrincipal AuthenticatedUser user,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(productService.getOne(user.tenantId(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@AuthenticationPrincipal AuthenticatedUser user,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(user.tenantId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user,
                                       @PathVariable Long id) {
        productService.delete(user.tenantId(), id);
        return ResponseEntity.noContent().build();
    }
}
