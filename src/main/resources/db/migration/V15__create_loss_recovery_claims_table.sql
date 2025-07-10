CREATE TABLE loss_recovery_claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    merchant_id BIGINT,
    contract_id BIGINT NOT NULL,
    loss_type VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(50) NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES users(id),
    CONSTRAINT fk_merchant FOREIGN KEY (merchant_id) REFERENCES users(id),
    CONSTRAINT fk_contract FOREIGN KEY (contract_id) REFERENCES contracts(id)
);
