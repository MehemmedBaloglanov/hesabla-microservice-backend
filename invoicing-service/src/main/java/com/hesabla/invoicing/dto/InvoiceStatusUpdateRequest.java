package com.hesabla.invoicing.dto;

import com.hesabla.invoicing.domain.InvoiceStatus;
import jakarta.validation.constraints.NotNull;

public record InvoiceStatusUpdateRequest(
        @NotNull(message = "Status göstərilməlidir")
        InvoiceStatus status
) {
}
