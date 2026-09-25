package com.hesabla.notification.listener;

import com.hesabla.notification.event.InvoiceCreatedEvent;
import com.hesabla.notification.event.InvoiceStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InvoiceEventListener {

    @KafkaListener(topics = "invoice-created", groupId = "hesabla-notification-service")
    public void handleInvoiceCreated(InvoiceCreatedEvent event) {
        log.info("[BİLDİRİŞ] Yeni faktura yaradıldı: {} (tenant={}, məbləğ={} AZN) — email simulyasiyası: müştəriyə bildiriş göndərildi.",
                event.invoiceNumber(), event.tenantId(), event.totalAmount());
    }

    @KafkaListener(topics = "invoice-status-changed", groupId = "hesabla-notification-service")
    public void handleInvoiceStatusChanged(InvoiceStatusChangedEvent event) {
        log.info("[BİLDİRİŞ] Faktura statusu dəyişdi: {} — {} → {} — email simulyasiyası: müştəriyə bildiriş göndərildi.",
                event.invoiceNumber(), event.oldStatus(), event.newStatus());
    }
}