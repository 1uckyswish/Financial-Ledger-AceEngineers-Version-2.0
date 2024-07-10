# Financial Transaction Tracker Capstone 1 Refactor

Welcome to the Financial Transaction Tracker repository! This project represents a significant refactor of our initial Java capstone project, originally designed as a static application for managing financial transactions stored in CSV format. This project started as a solo assignment but ended up transforming it into a collaborative effort, introducing powerful enhancements to functionality and usability.
### [View Part 1 Here](https://github.com/1uckyswish/financial-ledger)
## Project Evolution

### From Static to Dynamic

![Screenshot 2024-07-10 at 2 04 00 PM](https://github.com/1uckyswish/Financial-Ledger-AceEngineers-Version-2.0/assets/107441301/9d98ff81-cb45-45d1-bb6b-8c33794a066e)

Previously relying on static CSV files, we've migrated to a MySQL database backend. This transition allows for real-time data management, improving scalability, reliability, and performance. Now, all transactions are securely stored and managed within the MySQL database, ensuring data integrity and enabling advanced querying capabilities.

### Enhanced Features

#### User Authentication and Registration

![Screenshot 2024-07-10 at 2 05 51 PM](https://github.com/1uckyswish/Financial-Ledger-AceEngineers-Version-2.0/assets/107441301/938612a5-bb72-4cc6-b7ea-013a615024e9)
![Screenshot 2024-07-10 at 2 05 09 PM](https://github.com/1uckyswish/Financial-Ledger-AceEngineers-Version-2.0/assets/107441301/26376c8a-9519-4e15-b5e6-1d018b3cf369)

We've introduced robust user authentication and registration features, enhancing security and personalization. Users can securely log in, access their personalized accounts, and manage transactions seamlessly.

#### Real-time Updates and Error Handling

![Screenshot 2024-07-10 at 2 07 05 PM](https://github.com/1uckyswish/Financial-Ledger-AceEngineers-Version-2.0/assets/107441301/d45e11cc-a864-443f-9fc7-6d3adb37b7be)

Integration with the MySQL database connector enables live updates, ensuring that transactions are processed and reflected instantaneously. We've also bolstered error handling mechanisms, particularly around user authentication, to provide a smooth and reliable user experience.

## Key Features

### Interactive Home Screen

![Screenshot 2024-07-10 at 2 18 22 PM](https://github.com/1uckyswish/Financial-Ledger-AceEngineers-Version-2.0/assets/107441301/46f33549-7c6d-4201-b730-8a0ee35b46a2)

Upon launching the application, users are greeted with an intuitive home screen offering:

- **Deposit Management (D)**: Add deposits directly to your account with immediate database updates.
  
- **Payment Tracking (P)**: Record payments instantly, maintaining accurate transaction histories.
  
- **Balance Overview (B)**: View your current balance in real-time, separating income and expenses dynamically.

### User-Focused Enhancements

![Screenshot 2024-07-10 at 2 12 07 PM](https://github.com/1uckyswish/Financial-Ledger-AceEngineers-Version-2.0/assets/107441301/2b79f301-f56a-412a-9a99-af4437074df2)

- **Secure Login**: Advanced authentication ensures data security and user privacy.
  
- **Effortless Registration**: New users can register seamlessly, creating accounts for personalized transaction tracking.

## Setting Up

To start using the Financial Transaction Tracker, follow these steps:

1. **Prerequisites**:
   - Ensure Java and MySQL are installed on your system.

2. **Clone the Repository**:
   - `git clone https://github.com/1uckyswish/Financial-Ledger-AceEngineers-Version-2.0.git`
   - `cd financial-transaction-tracker`
   
3. **Import MySQL Script**:
   - Open MySQL Workbench.
   - Import the provided MySQL script (`aceBank.sql`) included in the repository. This script sets up the necessary database schema and initial data.

4. **Configure MySQL Connection**:
   - Update the MySQL connection settings in your Java application (`DatabaseConnector.java` or similar configuration file) to match your MySQL server credentials.

5. **Run the Application**:
   - Build and run the application to start managing your transactions effectively.

## Team Members

Meet the dedicated team behind the Financial Transaction Tracker:

- **[Noel](https://github.com/1uckyswish)**: GitHub repository owner and project lead.
- **[Tina](https://github.com/twentyfive21)**: Implementing authentication and security features.
- **[Zamir](https://github.com/ZPollar0)**: Contributing to database integration and backend development.
- **[Staphon](https://github.com/StaphonP)**: Enhancing user interface and user experience design.

