-- Add price column to transactions if missing
ALTER TABLE transactions ADD COLUMN price DECIMAL(15,2) NULL;
