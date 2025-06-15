-- Insert test users
INSERT INTO users (username, password, role) VALUES
('john_farmer', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4.', 'ROLE_FARMER'), -- password: farmer123
('sarah_farmer', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4.', 'ROLE_FARMER'),
('mike_merchant', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4.', 'ROLE_MERCHANT'), -- password: merchant123
('lisa_merchant', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4.', 'ROLE_MERCHANT'),
('admin_user', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4.', 'ROLE_ADMIN'); -- password: admin123

-- Insert products for farmers
INSERT INTO products (farmer_id, name, quantity, quality, created_at) VALUES
((SELECT id FROM users WHERE username = 'john_farmer'), 'Wheat', 1000, 'A', NOW()),
((SELECT id FROM users WHERE username = 'john_farmer'), 'Corn', 500, 'A', NOW()),
((SELECT id FROM users WHERE username = 'sarah_farmer'), 'Soybeans', 750, 'B', NOW()),
((SELECT id FROM users WHERE username = 'sarah_farmer'), 'Rice', 1200, 'A', NOW());

-- Insert base prices
INSERT INTO base_prices (product_name, price, effective_date) VALUES
('Wheat', 320.00, NOW()),
('Corn', 280.00, NOW()),
('Soybeans', 450.00, NOW()),
('Rice', 400.00, NOW());

-- Insert contracts
INSERT INTO contracts (merchant_id, title, description, product, quantity, base_price, quality_grade, 
                      bidding_end_date, delivery_date, status, terms) VALUES
-- Active contracts for mike_merchant
((SELECT id FROM users WHERE username = 'mike_merchant'),
 'Premium Wheat Contract', 
 'Looking for high-quality wheat with protein content above 12%',
 'Wheat',
 500.00,
 325.00,
 'A',
 DATE_ADD(NOW(), INTERVAL 30 DAY),
 DATE_ADD(NOW(), INTERVAL 90 DAY),
 'OPEN',
 'Payment within 15 days of delivery. Quality testing will be performed at delivery.'),

((SELECT id FROM users WHERE username = 'mike_merchant'),
 'Organic Corn Supply',
 'Seeking organic corn supply for food processing',
 'Corn',
 300.00,
 290.00,
 'A',
 DATE_ADD(NOW(), INTERVAL 15 DAY),
 DATE_ADD(NOW(), INTERVAL 60 DAY),
 'OPEN',
 'Must be certified organic. Storage conditions will be verified.'),

-- Active contracts for lisa_merchant
((SELECT id FROM users WHERE username = 'lisa_merchant'),
 'Soybean Contract Q3',
 'Large soybean contract for oil production',
 'Soybeans',
 1000.00,
 460.00,
 'B',
 DATE_ADD(NOW(), INTERVAL 45 DAY),
 DATE_ADD(NOW(), INTERVAL 120 DAY),
 'OPEN',
 'Multiple delivery dates available. Price includes transportation.'),

((SELECT id FROM users WHERE username = 'lisa_merchant'),
 'Premium Rice Contract',
 'Seeking high-quality rice for export',
 'Rice',
 800.00,
 410.00,
 'A',
 DATE_ADD(NOW(), INTERVAL 20 DAY),
 DATE_ADD(NOW(), INTERVAL 75 DAY),
 'OPEN',
 'Export quality requirements must be met. Packaging included.');

-- Insert bids
INSERT INTO bids (contract_id, farmer_id, amount, proposal, created_at) VALUES
-- John's bids
((SELECT id FROM contracts WHERE title = 'Premium Wheat Contract'),
 (SELECT id FROM users WHERE username = 'john_farmer'),
 320.00,
 'Can supply high-quality wheat meeting all specifications. Available for early delivery.',
 NOW()),

((SELECT id FROM contracts WHERE title = 'Organic Corn Supply'),
 (SELECT id FROM users WHERE username = 'john_farmer'),
 285.00,
 'Certified organic corn available. Can provide all necessary documentation.',
 NOW()),

-- Sarah's bids
((SELECT id FROM contracts WHERE title = 'Soybean Contract Q3'),
 (SELECT id FROM users WHERE username = 'sarah_farmer'),
 455.00,
 'Can fulfill the entire contract quantity. Flexible with delivery schedule.',
 NOW()),

((SELECT id FROM contracts WHERE title = 'Premium Rice Contract'),
 (SELECT id FROM users WHERE username = 'sarah_farmer'),
 405.00,
 'Export quality rice ready for shipping. Can start delivery within 30 days.',
 NOW());

-- Insert messages
INSERT INTO messages (sender_id, recipient_id, subject, content, `read`, created_at) VALUES
-- Conversations between John and Mike
((SELECT id FROM users WHERE username = 'john_farmer'),
 (SELECT id FROM users WHERE username = 'mike_merchant'),
 'Question about Wheat Contract',
 'Hi, I have some questions about the quality testing process for the wheat contract.',
 0,
 NOW()),

((SELECT id FROM users WHERE username = 'mike_merchant'),
 (SELECT id FROM users WHERE username = 'john_farmer'),
 'Re: Question about Wheat Contract',
 'Sure, we use standard industry testing procedures. Would you like to discuss the details?',
 0,
 DATE_ADD(NOW(), INTERVAL 1 HOUR)),

-- Conversations between Sarah and Lisa
((SELECT id FROM users WHERE username = 'sarah_farmer'),
 (SELECT id FROM users WHERE username = 'lisa_merchant'),
 'Rice Contract Delivery Schedule',
 'I would like to discuss the possibility of splitting the rice delivery into multiple shipments.',
 0,
 NOW()),

((SELECT id FROM users WHERE username = 'lisa_merchant'),
 (SELECT id FROM users WHERE username = 'sarah_farmer'),
 'Re: Rice Contract Delivery Schedule',
 'That should be possible. Let\'s discuss the specific dates that work for both parties.',
 0,
 DATE_ADD(NOW(), INTERVAL 2 HOUR));
