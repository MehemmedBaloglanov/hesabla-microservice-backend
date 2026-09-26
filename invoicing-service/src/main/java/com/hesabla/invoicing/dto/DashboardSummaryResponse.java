package com.hesabla.invoicing.dto;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardSummaryResponse(
        long totalInvoices,
        BigDecimal totalRevenue,
        BigDecimal totalOutstanding,
        Map<String, Long> countByStatus
) {
}
