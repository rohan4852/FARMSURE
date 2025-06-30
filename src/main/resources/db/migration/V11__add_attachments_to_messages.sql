ALTER TABLE messages
ADD COLUMN attachment_file_name VARCHAR(255),
ADD COLUMN attachment_file_type VARCHAR(255),
ADD COLUMN attachment_file_url VARCHAR(255);
