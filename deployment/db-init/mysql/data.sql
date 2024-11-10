-- Insert data into users
INSERT INTO users (name, email, phone_number, created_at, updated_at) VALUES
('Alice Smith', 'alice@example.com', '1234567890', NOW(), NOW()),
('Bob Johnson', 'bob@example.com', '0987654321', NOW(), NOW()),
('Charlie Brown', 'charlie@example.com', '1122334455', NOW(), NOW()),
('David Wilson', 'david@example.com', '5566778899', NOW(), NOW()),
('Eve Davis', 'eve@example.com', '6677889900', NOW(), NOW());

-- Insert data into hotels
INSERT INTO hotels (hotel_name, hotel_type, available_from, available_to, description, status, created_at, updated_at) VALUES
('Sunset Resort', 'Resort', '2024-01-01', '2024-12-31', 'Luxury beachfront resort with stunning ocean views.', TRUE, NOW(), NOW()),
('Mountain Inn', 'Hotel', '2024-02-01', '2024-11-30', 'Cozy mountain retreat with hiking trails nearby.', TRUE, NOW(), NOW()),
('City Lights Hotel', 'Hotel', '2024-03-15', '2024-09-30', 'Modern hotel in the heart of downtown.', FALSE, NOW(), NOW()),
('Lakeside Lodge', 'Lodge', '2024-04-01', '2024-10-15', 'Charming lodge by the lake with water activities.', TRUE, NOW(), NOW()),
('Desert Oasis', 'Resort', '2024-05-01', '2024-12-15', 'Exclusive desert resort with luxury amenities.', TRUE, NOW(), NOW());

-- Insert data into reservations
INSERT INTO reservations (user_id, hotel_id, check_in_date, check_out_date, total_price, num_of_guests, status, created_at, updated_at) VALUES
(1, 1, '2023-06-15', '2023-06-18', 600.00, 2, 'CONFIRMED', NOW(), NOW()),
(2, 2, '2023-07-01', '2023-07-05', 400.00, 4, 'PENDING', NOW(), NOW()),
(3, 3, '2023-08-10', '2023-08-14', 500.00, 3, 'CONFIRMED', NOW(), NOW()),
(4, 4, '2023-09-05', '2023-09-07', 150.00, 1, 'CANCELLED', NOW(), NOW());



