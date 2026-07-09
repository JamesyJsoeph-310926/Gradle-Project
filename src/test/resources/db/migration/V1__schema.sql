CREATE TABLE order_statuses (
    code VARCHAR(20) PRIMARY KEY,
    description VARCHAR(100) NOT NULL
);

CREATE TABLE retail_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    total_paise BIGINT NOT NULL CHECK (total_paise > 0),
    ordered_on DATE NOT NULL,
    refunded BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_status
        FOREIGN KEY (status)
        REFERENCES order_statuses(code)
);

CREATE TABLE retail_order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    sku VARCHAR(40) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES retail_orders(id)
        ON DELETE CASCADE
);