-- Add status column to bids table if it doesn't exist
SET @db_name = DATABASE();

SELECT COUNT(*)
INTO @exists
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @db_name
    AND TABLE_NAME = 'bids'
    AND COLUMN_NAME = 'status';

SET @query = IF(@exists = 0,
    'ALTER TABLE bids ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT "PENDING"',
    'SELECT 1');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Update existing bids without status
UPDATE bids SET status = 'PENDING' WHERE status IS NULL;
