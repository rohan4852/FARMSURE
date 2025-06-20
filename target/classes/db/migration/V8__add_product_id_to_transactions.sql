-- Add product_id column to transactions if missing
SET @col := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='transactions' AND COLUMN_NAME='product_id' AND TABLE_SCHEMA=DATABASE());
SET @sql := IF(@col = 0, 'ALTER TABLE transactions ADD COLUMN product_id BIGINT NULL', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
