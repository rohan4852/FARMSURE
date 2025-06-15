-- Add indexes for better performance
ALTER TABLE contracts 
ADD INDEX idx_merchant_id (merchant_id),
ADD INDEX idx_assigned_farmer (assigned_farmer_id),
ADD INDEX idx_status (status),
ADD INDEX idx_product (product);

-- Add indexes for bids table
ALTER TABLE bids 
ADD INDEX idx_contract_farmer (contract_id, farmer_id),
ADD INDEX idx_status (status);

-- Add new columns if they don't exist
SET @db_name = DATABASE();

-- Check and add merchant_id
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'merchant_id';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN merchant_id BIGINT AFTER id',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add product
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'product';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN product VARCHAR(255) NOT NULL AFTER description',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add quantity
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'quantity';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN quantity DOUBLE NOT NULL AFTER product',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add base_price
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'base_price';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN base_price DOUBLE NOT NULL AFTER quantity',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add quality_grade
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'quality_grade';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN quality_grade VARCHAR(255) NOT NULL AFTER base_price',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add bidding_end_date
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'bidding_end_date';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN bidding_end_date TIMESTAMP NOT NULL AFTER quality_grade',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add delivery_date
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'delivery_date';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN delivery_date TIMESTAMP NOT NULL AFTER bidding_end_date',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add terms
SELECT COUNT(*) 
INTO @exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'contracts'
    AND COLUMN_NAME = 'terms';

SET @query = IF(@exists = 0,
    'ALTER TABLE contracts ADD COLUMN terms TEXT AFTER delivery_date',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add foreign key constraints
ALTER TABLE contracts
ADD CONSTRAINT fk_contracts_merchant FOREIGN KEY (merchant_id) REFERENCES users(id),
ADD CONSTRAINT fk_contracts_farmer FOREIGN KEY (assigned_farmer_id) REFERENCES users(id);
