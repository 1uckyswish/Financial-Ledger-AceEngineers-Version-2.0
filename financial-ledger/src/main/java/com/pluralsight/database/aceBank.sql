DROP DATABASE IF EXISTS aceBank;

CREATE DATABASE IF NOT EXISTS aceBank;

USE aceBank;

CREATE TABLE users (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
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
VALUES
(1, '2023-08-07 08:30:00', 'Salary Payment', 'Apple Inc.', 3500.00),
(1, '2024-01-15 09:45:00', 'Utilities Bill Payment', 'Duke Energy', -150.00),
(1, '2023-11-21 10:20:00', 'Online Shopping', 'Walmart Inc.', -89.99),
(1, '2023-11-06 14:30:00', 'Stock Sale Proceeds', 'Robinhood', 500.00),
(1, '2024-01-28 16:00:00', 'Bonus Payment', 'Company XYZ', 1000.00),
(1, '2023-09-10 18:45:00', 'Consulting Fee', 'Freelancer Inc.', 700.00),
(1, '2024-05-01 09:30:15', 'Monthly Subscription Renewal', 'Netflix', -15.99),
(1, '2024-05-01 12:00:00', 'Salary Deposit', 'Tech Innovations Inc.', 2500.00),
(1, '2024-05-02 16:20:10', 'Bookstore Purchase', 'Barnes & Noble', -27.50),

(2, '2023-05-03 11:30:00', 'Rent Payment', 'Real Estate Management', -1200.00),
(2, '2024-04-03 12:45:00', 'Freelance Work Payment', 'Fiverr', 800.00),
(2, '2023-09-29 14:00:00', 'Dinner Expense', 'Chipotle Mexican Grill', -45.50),
(2, '2023-06-27 20:15:00', 'Cashback Rewards', 'Discover Financial Services', 50.00),
(2, '2023-04-19 09:00:00', 'Selling Old Items', 'eBay Inc.', 150.00),
(2, '2024-04-19 11:20:00', 'Refund from Overcharged Bill', 'Utility Co.', 75.00),
(2, '2024-05-02 14:30:00', 'Investment Dividend Received', 'Vanguard Group', 150.00),
(2, '2024-05-03 19:30:55', 'Movie Night Out', 'AMC Theatres', -45.00),
(2, '2024-05-03 11:45:00', 'Website Ad Revenue', 'Alphabet Inc. (Google)', 300.00),

(3, '2023-10-10 15:30:00', 'Credit Card Payment', 'Visa Inc.', -200.00),
(3, '2023-07-19 16:45:00', 'Gasoline Expense', 'Exxon Mobil Corporation', -40.00),
(3, '2024-02-18 18:00:00', 'Internet Subscription Renewal', 'AT&T Inc.', -75.00),
(3, '2023-12-25 13:55:00', 'Gift Received', 'Family Member', 200.00),
(3, '2024-02-15 11:15:50', 'Gift Given To Sick Granny', 'Charity', -800.00),
(3, '2024-04-21 16:40:11', 'Money Found In Back Pocket', 'Noel', 20.00),
(3, '2024-05-02 15:27:04', 'Donation from Teacher', 'Ezra Williams', 300.00),
(3, '2024-05-02 15:27:28', 'New MacBook for School', 'Apple Inc.', -1245.54),

(4, '2023-06-04 19:15:00', 'Movie Tickets Purchase', 'AMC Theatres', -30.00),
(4, '2023-12-24 20:30:00', 'Grocery Shopping', 'The Kroger Co.', -100.00),
(4, '2024-03-20 21:45:00', 'Medical Bill Payment', 'UnitedHealth Group', -350.00),
(4, '2023-08-27 23:00:00', 'Withdrawal from ATM', 'Bank of America', -200.00),
(4, '2024-04-21 16:48:51', 'Purchase at Disney Land', 'Disney World', -50.00),
(4, '2024-04-21 21:45:37', 'Interest Received', 'Chase Bank', 129.54),
(4, '2024-04-21 21:47:38', 'Dinner Expense', 'The Cheesecake Factory', -88.98),

(5, '2023-04-28 08:30:00', 'Interest Received', 'Wells Fargo & Company', 50.00),
(5, '2023-05-30 09:45:00', 'Online Course Payment', 'Udemy', -199.00),
(5, '2024-03-09 10:00:00', 'Investment Dividend', 'Vanguard Group', 250.00),
(5, '2024-04-21 07:04:23', 'Rent Payment', 'Allen Tate', -2340.05),
(5, '2024-04-27 21:12:30', 'Grocery Shopping', 'Costco', -74.98),
(5, '2024-04-27 05:23:33', 'Dinner with Father-in-Law', 'Chick Fil-A', 23.66);


/* SELECT * FROM aceBank.transactions INNER JOIN aceBank.users
ON aceBank.transactions.user_id = aceBank.users.user_id
WHERE aceBank.transactions.user_id = 1; */
