DROP DATABASE IF EXISTS aceBank;

CREATE DATABASE IF NOT EXISTS aceBank;

USE aceBank;

CREATE TABLE users (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(25) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE transactions (
    transaction_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    date DATETIME NOT NULL,
    description VARCHAR(200) NOT NULL,
    vendor VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


INSERT INTO users (username, password) 
VALUES  ('Tina','123'),
        ('Zamir','123'),
        ('Staphon','123'),
		('Noel','123'),
		('Kellen','123');
        
INSERT INTO transactions (user_id, date, description, vendor, amount) 
VALUES  (1, '2023-08-01 08:30:00', 'Salary Payment', 'Apple Inc.', 3500.00),
(2, '2024-01-15 09:45:00', 'Utilities Bill', 'Duke Energy', 150.05),
(3, '2024-02-20 01:25:00', 'Consulting Fee', 'Freelancer Inc.', 750.00),
(2, '2024-08-01 03:30:00', 'Salary Payment', 'Google Inc.', 5500.00),
(2, '2024-02-01 06:30:00', 'School Payment', 'Year Up', 9500.00),
 (4, '2024-08-01 06:30:00', 'Ice Cream', 'Mcdonalds', 5500.00);
        
/* SELECT * FROM aceBank.transactions INNER JOIN aceBank.users
ON aceBank.transactions.user_id = aceBank.users.user_id
WHERE aceBank.transactions.user_id = 1; */
