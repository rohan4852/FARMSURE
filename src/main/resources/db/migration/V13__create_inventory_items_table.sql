-- Flyway migration script to create inventory_items table

CREATE TABLE inventory_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    farmer_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quality VARCHAR(255),
    weight DOUBLE,
    storage_location VARCHAR(255),
    dispatch_date TIMESTAMP,
    quantity INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_contract FOREIGN KEY (contract_id) REFERENCES contracts(id),
    CONSTRAINT fk_inventory_farmer FOREIGN KEY (farmer_id) REFERENCES users(id)
);
