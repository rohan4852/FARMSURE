-- Flyway migration script to create wastage_loss table

CREATE TABLE wastage_loss (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inventory_item_id BIGINT NOT NULL,
    wastage_quantity DOUBLE NOT NULL,
    loss_quantity DOUBLE NOT NULL,
    recorded_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_wastage_inventory FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id)
);
