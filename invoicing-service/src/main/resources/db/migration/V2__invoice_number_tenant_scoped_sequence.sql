-- 1) Faktura nömrəsi unikallığını QLOBAL-dan TENANT-DAXİLİ-yə dəyişirik.
--    Səbəb: iki fərqli tenant eyni ildə öz ilk fakturasını yaratdıqda
--    hər ikisi "INV-2026-00001" generasiya edir — bu, real mühasibatlıq
--    təcrübəsinə uyğundur (hər şirkətin öz nömrələmə sırası var) və
--    əvvəlki qlobal UNIQUE constraint bunu qadağan edirdi (qarantiyalı toqquşma).
ALTER TABLE invoices DROP CONSTRAINT invoices_invoice_number_key;
ALTER TABLE invoices ADD CONSTRAINT uk_invoices_tenant_invoice_number UNIQUE (tenant_id, invoice_number);

-- 2) Hər tenant üçün ayrıca, DB-səviyyəsində ATOMİK sayğac cədvəli.
--    Səbəb: əvvəlki `countByTenantId(tenantId) + 1` məntiqi paralel
--    (concurrent) faktura yaratma sorğuları altında race condition-a
--    açıq idi (iki paralel sorğu eyni "count"-u oxuyub eyni nömrəni
--    generasiya edə bilərdi). İndi Postgres-in UPSERT + RETURNING
--    mexanizmi sətir kilidləməsi vasitəsilə bunu təhlükəsiz sıralayır.
CREATE TABLE invoice_sequences (
                                   tenant_id BIGINT PRIMARY KEY,
                                   last_number BIGINT NOT NULL DEFAULT 0
);

-- Mövcud tenant-ların hazırkı faktura sayını ilkin dəyər kimi köçürürük ki,
-- yeni sıra köhnə nömrələrlə üst-üstə düşməsin.
INSERT INTO invoice_sequences (tenant_id, last_number)
SELECT tenant_id, COUNT(*) FROM invoices GROUP BY tenant_id;