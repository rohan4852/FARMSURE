-- Add quantity column to transactions if missing
SET @col := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='transactions' AND COLUMN_NAME='quantity' AND TABLE_SCHEMA=DATABASE());
SET @sql := IF(@col = 0, 'ALTER TABLE transactions ADD COLUMN quantity INT NULL', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add transaction_date column to transactions if missing
SET @col := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='transactions' AND COLUMN_NAME='transaction_date' AND TABLE_SCHEMA=DATABASE());
SET @sql := IF(@col = 0, 'ALTER TABLE transactions ADD COLUMN transaction_date DATETIME NULL', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
