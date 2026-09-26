package com.hesabla.invoicing.controller;

import com.hesabla.invoicing.dto.*;
import com.hesabla.invoicing.security.AuthenticatedUser;
import com.hesabla.invoicing.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@AuthenticationPrincipal AuthenticatedUser user,
                                                  @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.create(user.tenantId(), user.userId(), request));
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummaryResponse> dashboardSummary(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(invoiceService.getDashboardSummary(user.tenantId()));
    }

    @GetMapping
    public ResponseEntity<PageResponse<InvoiceResponse>> listAll(@AuthenticationPrincipal AuthenticatedUser user,
                                                                 @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(invoiceService.listAll(user.tenantId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getOne(@AuthenticationPrincipal AuthenticatedUser user,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getOne(user.tenantId(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> update(@AuthenticationPrincipal AuthenticatedUser user,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.update(user.tenantId(), id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InvoiceResponse> updateStatus(@AuthenticationPrincipal AuthenticatedUser user,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody InvoiceStatusUpdateRequest request) {
        return ResponseEntity.ok(invoiceService.updateStatus(user.tenantId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user,
                                       @PathVariable Long id) {
        invoiceService.delete(user.tenantId(), id);
        return ResponseEntity.noContent().build();
    }
}