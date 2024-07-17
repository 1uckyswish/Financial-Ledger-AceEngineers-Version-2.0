-- Drop the existing database if it exists to start fresh
DROP DATABASE IF EXISTS aceBank;

-- Create a new database named 'aceBank' if it does not already exist
CREATE DATABASE IF NOT EXISTS aceBank;

-- Use the newly created 'aceBank' database
USE aceBank;

-- Create a 'users' table with columns for user_id, username, and password
CREATE TABLE users (
    user_id INT NOT NULL AUTO_INCREMENT, -- Unique user ID
    username VARCHAR(50) NOT NULL UNIQUE, -- Username which must be unique
    password VARCHAR(25) NOT NULL, -- User password
    PRIMARY KEY (user_id) -- Set user_id as the primary key
);

-- Create a 'transactions' table with columns for transaction_id, user_id, date, description, vendor, and amount
CREATE TABLE transactions (
    transaction_id INT NOT NULL AUTO_INCREMENT, -- Unique transaction ID
    user_id INT NOT NULL, -- ID of the user making the transaction
    date DATETIME NOT NULL, -- Date and time of the transaction
    description VARCHAR(200) NOT NULL, -- Description of the transaction
    vendor VARCHAR(50) NOT NULL, -- Vendor involved in the transaction
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0, -- Amount of the transaction
    PRIMARY KEY (transaction_id), -- Set transaction_id as the primary key
    FOREIGN KEY (user_id) REFERENCES users(user_id) -- Foreign key referencing user_id in users table
);

-- Insert sample user data into the 'users' table
INSERT INTO users (username, password)
VALUES  ('Tina','123'),
        ('Zamir','123'),
        ('Staphon','123'),
        ('Noel','123'),
        ('Kellen','123');

-- Insert sample transaction data into the 'transactions' table
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
(1, '2024-06-01 12:00:00', 'Salary Deposit', 'Tech Innovations Inc.', 2500.00),
(1, '2024-06-05 10:30:45', 'Grocery Shopping', 'Whole Foods', -150.75),
(1, '2024-06-10 13:20:00', 'Utility Bill', 'City Water Department', -65.00),
(1, '2024-06-15 09:15:30', 'Coffee Shop', 'Starbucks', -15.00),
(1, '2024-07-01 12:00:00', 'Salary Deposit', 'Tech Innovations Inc.', 2500.00),
(1, '2024-07-05 16:45:20', 'Grocery Shopping', 'Trader Joe\'s', -85.50),
(1, '2024-07-12 14:15:30', 'Restaurant', 'Olive Garden', -45.60),
(1, '2024-07-14 11:00:00', 'Utility Bill', 'City Water Department', -65.00),


(2, '2023-05-03 11:30:00', 'Rent Payment', 'Real Estate Management', -1200.00),
(2, '2024-04-03 12:45:00', 'Freelance Work Payment', 'Fiverr', 800.00),
(2, '2023-09-29 14:00:00', 'Dinner Expense', 'Chipotle Mexican Grill', -45.50),
(2, '2023-06-27 20:15:00', 'Cashback Rewards', 'Discover Financial Services', 50.00),
(2, '2023-04-19 09:00:00', 'Selling Old Items', 'eBay Inc.', 150.00),
(2, '2024-04-19 11:20:00', 'Refund from Overcharged Bill', 'Utility Co.', 75.00),
(2, '2024-05-02 14:30:00', 'Investment Dividend Received', 'Vanguard Group', 150.00),
(2, '2024-05-03 19:30:55', 'Movie Night Out', 'AMC Theatres', -45.00),
(2, '2024-05-03 11:45:00', 'Website Ad Revenue', 'Alphabet Inc. (Google)', 300.00),
(2, '2024-06-01 12:00:00', 'Salary Deposit', 'Creative Solutions Ltd.', 2800.00),
(2, '2024-06-03 09:45:20', 'Clothing Purchase', 'H&M', -75.00),
(2, '2024-06-08 16:50:00', 'Bookstore Purchase', 'Barnes & Noble', -27.50),
(2, '2024-06-13 11:20:33', 'Pharmacy', 'CVS Pharmacy', -20.00),
(2, '2024-07-01 12:00:00', 'Salary Deposit', 'Creative Solutions Ltd.', 2800.00),
(2, '2024-07-06 13:35:15', 'Clothing Purchase', 'Zara', -100.00),
(2, '2024-07-08 18:50:55', 'Electronics Purchase', 'Best Buy', -320.00),
(2, '2024-07-15 10:20:00', 'Coffee Shop', 'Starbucks', -12.00),

(3, '2023-10-10 15:30:00', 'Credit Card Payment', 'Visa Inc.', -200.00),
(3, '2023-07-19 16:45:00', 'Gasoline Expense', 'Exxon Mobil Corporation', -40.00),
(3, '2024-02-18 18:00:00', 'Internet Subscription Renewal', 'AT&T Inc.', -75.00),
(3, '2023-12-25 13:55:00', 'Gift Received', 'Family Member', 200.00),
(3, '2024-02-15 11:15:50', 'Gift Given To Sick Granny', 'Charity', -800.00),
(3, '2024-04-21 16:40:11', 'Money Found In Back Pocket', 'Noel', 20.00),
(3, '2024-05-02 15:27:04', 'Donation from Teacher', 'Ezra Williams', 300.00),
(3, '2024-05-02 15:27:28', 'New MacBook for School', 'Apple Inc.', -1245.54),
(3, '2024-06-01 12:00:00', 'Salary Deposit', 'Marketing Experts Inc.', 3000.00),
(3, '2024-06-04 11:20:33', 'Coffee Shop', 'Starbucks', -15.30),
(3, '2024-06-09 10:00:00', 'Restaurant', 'Chili\'s', -35.00),
(3, '2024-06-14 15:10:10', 'Online Subscription', 'Netflix', -13.99),
(3, '2024-07-01 12:00:00', 'Salary Deposit', 'Marketing Experts Inc.', 3000.00),
(3, '2024-07-05 16:30:00', 'Grocery Shopping', 'Whole Foods', -120.50),
(3, '2024-07-09 16:10:10', 'Bookstore Purchase', 'Barnes & Noble', -27.50),
(3, '2024-07-13 14:25:40', 'Utility Bill', 'Electric Company', -80.00),


(4, '2023-06-04 19:15:00', 'Movie Tickets Purchase', 'AMC Theatres', -30.00),
(4, '2023-12-24 20:30:00', 'Grocery Shopping', 'The Kroger Co.', -100.00),
(4, '2024-03-20 21:45:00', 'Medical Bill Payment', 'UnitedHealth Group', -350.00),
(4, '2023-08-27 23:00:00', 'Withdrawal from ATM', 'Bank of America', -200.00),
(4, '2024-04-21 16:48:51', 'Purchase at Disney Land', 'Disney World', -50.00),
(4, '2024-04-21 21:45:37', 'Interest Received', 'Chase Bank', 129.54),
(4, '2024-04-21 21:47:38', 'Dinner Expense', 'The Cheesecake Factory', -88.98),
(4, '2024-06-01 12:00:00', 'Salary Deposit', 'Financial Advisers LLC', 3200.00),
(4, '2024-06-02 12:30:40', 'Gas Station', 'Shell', -40.00),
(4, '2024-06-07 08:20:00', 'Movie Tickets', 'AMC Theatres', -25.00),
(4, '2024-06-11 15:05:25', 'Gym Membership', 'Gold\'s Gym', -60.00),
(4, '2024-07-01 12:00:00', 'Salary Deposit', 'Financial Advisers LLC', 3200.00),
(4, '2024-07-05 09:30:10', 'Coffee Shop', 'Dunkin\' Donuts', -10.00),
(4, '2024-07-14 09:45:30', 'Pet Supplies', 'Petco', -80.00),
(4, '2024-07-16 18:15:45', 'Restaurant', 'Applebee\'s', -40.00),

(5, '2023-04-28 08:30:00', 'Interest Received', 'Wells Fargo & Company', 50.00),
(5, '2023-05-30 09:45:00', 'Online Course Payment', 'Udemy', -199.00),
(5, '2024-03-09 10:00:00', 'Investment Dividend', 'Vanguard Group', 250.00),
(5, '2024-04-21 07:04:23', 'Rent Payment', 'Allen Tate', -2340.05),
(5, '2024-04-27 21:12:30', 'Grocery Shopping', 'Costco', -74.98),
(5, '2024-04-27 05:23:33', 'Dinner with Father-in-Law', 'Chick Fil-A', 23.66),
(5, '2024-06-01 12:00:00', 'Salary Deposit', 'Global Tech Partners', 2900.00),
(5, '2024-06-03 17:45:30', 'Movie Tickets', 'AMC Theatres', -25.00),
(5, '2024-06-08 11:30:00', 'Grocery Shopping', 'Trader Joe\'s', -130.00),
(5, '2024-06-12 14:10:20', 'Clothing Purchase', 'Macy\'s', -90.00),
(5, '2024-07-01 12:00:00', 'Salary Deposit', 'Global Tech Partners', 2900.00),
(5, '2024-07-05 13:40:10', 'Electronics Purchase', 'Apple Store', -450.00),
(5, '2024-07-13 13:30:10', 'Pet Supplies', 'Petco', -80.00),
(5, '2024-07-15 16:00:00', 'Utility Bill', 'City Water Department', -65.00),

-- for presentation purpose

(5, '2024-07-17 13:30:10', 'Stay at Hotel', 'Marriott', -150.00),
(5, '2024-07-17 19:00:00', 'Dining with family', 'Cheesecake Factory', -50.00),
(5, '2024-07-17 09:00:00', 'Salary Income', 'Year Up', 2000.00),
(5, '2024-07-17 11:00:00', 'Shopping Refund', 'Amazon', 75.00),
(5, '2024-07-17 17:00:00', 'Found Money In Back Pocket', 'Home', 20.00);
