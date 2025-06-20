-- Make all columns in transactions table nullable for safe migration
ALTER TABLE transactions 
MODIFY COLUMN farmer_id BIGINT NULL,
MODIFY COLUMN merchant_id BIGINT NULL,
MODIFY COLUMN price DECIMAL(15,2) NULL,
MODIFY COLUMN product_id BIGINT NULL,
MODIFY COLUMN quantity INT NULL,
MODIFY COLUMN transaction_date DATETIME NULL;
