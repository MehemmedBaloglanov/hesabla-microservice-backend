package com.hesabla.invoicing.repository;

import com.hesabla.invoicing.domain.InvoiceStatus;

import java.math.BigDecimal;

public interface InvoiceStatusAggregate {
    InvoiceStatus getStatus();
    Long getCount();
    BigDecimal getSum();
}
