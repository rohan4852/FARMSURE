-- Schema for contract_farming database

USE contract_farming;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Create messages table
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT,
    recipient_id BIGINT,
    subject VARCHAR(255) NOT NULL,
    content TEXT,
    `read` TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_messages_sender FOREIGN KEY (sender_id) REFERENCES users(id),
    CONSTRAINT fk_messages_recipient FOREIGN KEY (recipient_id) REFERENCES users(id)
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    quality VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_farmer FOREIGN KEY (farmer_id) REFERENCES users(id)
);

-- Create price_offers table
CREATE TABLE IF NOT EXISTS price_offers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT,
    product_id BIGINT,
    price DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_price_offers_merchant FOREIGN KEY (merchant_id) REFERENCES users(id),
    CONSTRAINT fk_price_offers_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT,
    merchant_id BIGINT,
    product_id BIGINT,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transactions_farmer FOREIGN KEY (farmer_id) REFERENCES users(id),
    CONSTRAINT fk_transactions_merchant FOREIGN KEY (merchant_id) REFERENCES users(id),
    CONSTRAINT fk_transactions_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Create base_prices table
CREATE TABLE IF NOT EXISTS base_prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    effective_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create contracts table
CREATE TABLE IF NOT EXISTS contracts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    creator_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    budget DOUBLE NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'OPEN',
    assigned_farmer_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contracts_creator FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT fk_contracts_assigned_farmer FOREIGN KEY (assigned_farmer_id) REFERENCES users(id)
);

-- Create bids table
CREATE TABLE IF NOT EXISTS bids (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    farmer_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    proposal TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bids_contract FOREIGN KEY (contract_id) REFERENCES contracts(id),
    CONSTRAINT fk_bids_farmer FOREIGN KEY (farmer_id) REFERENCES users(id)
);
