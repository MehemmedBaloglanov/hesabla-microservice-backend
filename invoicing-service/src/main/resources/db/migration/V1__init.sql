CREATE TABLE customers (
                           id BIGSERIAL PRIMARY KEY,
                           tenant_id BIGINT NOT NULL,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255),
                           phone VARCHAR(50),
                           address VARCHAR(500),
                           tax_id VARCHAR(50),
                           created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_customers_tenant_id ON customers(tenant_id);

CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          tenant_id BIGINT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(1000),
                          unit_price NUMERIC(12,2) NOT NULL,
                          unit VARCHAR(20) NOT NULL DEFAULT 'ədəd',
                          created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_products_tenant_id ON products(tenant_id);

CREATE TABLE invoices (
                          id BIGSERIAL PRIMARY KEY,
                          tenant_id BIGINT NOT NULL,
                          invoice_number VARCHAR(50) NOT NULL UNIQUE,
                          customer_id BIGINT NOT NULL REFERENCES customers(id),
                          issue_date DATE NOT NULL,
                          due_date DATE,
                          status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                          total_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
                          created_by_user_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_invoices_tenant_id ON invoices(tenant_id);
CREATE INDEX idx_invoices_customer_id ON invoices(customer_id);

CREATE TABLE invoice_lines (
                               id BIGSERIAL PRIMARY KEY,
                               invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
                               product_id BIGINT REFERENCES products(id),
                               description VARCHAR(500) NOT NULL,
                               quantity NUMERIC(12,3) NOT NULL,
                               unit_price NUMERIC(12,2) NOT NULL,
                               line_total NUMERIC(12,2) NOT NULL
);

CREATE INDEX idx_invoice_lines_invoice_id ON invoice_lines(invoice_id);