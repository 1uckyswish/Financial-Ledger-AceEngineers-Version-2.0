# Capstone 1 - Accounting Ledger Application

Welcome to the Financial Transaction Tracker repository! This CLI application, built using Java, is designed to help you efficiently track all financial transactions for either business or personal use. With easy-to-use screens and intuitive functionalities, managing your finances has never been more straightforward. This application provides a seamless experience for users to add deposits, make payments (debits), and view transaction ledgers. All transactions are stored in a CSV file named transactions.csv, ensuring easy access and data integrity.
## Main Class (Home)

This is the heart of our Accounting Ledger Application project. The Main class manages all store functionalities. Check out the Maven Java folder structure on the left to see how the project is organized.

![MavenFolder](https://github.com/1uckyswish/financial-ledger/assets/107442415/d1043f76-3285-493e-b4fe-30102195c8e6)

## Start of Program (Home Screen)

Upon launching the application, users are greeted with the home screen, offering various options to interact with the Accounting Ledger Application.

![Start](https://github.com/1uckyswish/financial-ledger/assets/107442415/b313fbdc-bfe3-44e2-b780-7532b4c8c252)

## Option Add Deposit (D)

Users can add a deposit by providing relevant information when prompted. The deposit details are then saved to the transactions.csv file.

![deposit](https://github.com/1uckyswish/financial-ledger/assets/107442415/fef22e1f-6d63-4c3e-a810-572de34adcfb)

## Option Make Payment (Debit) (P)

Users can make a payment (debit) by entering the required information when prompted. The payment details are then recorded in the transactions.csv file.

![payment](https://github.com/1uckyswish/financial-ledger/assets/107442415/27986f32-368e-4911-99a3-55b61936903f)

## Option Balance (B) View Ledger Balance

This feature helps users see their balance by separating expenses and income. It also shows the total amount left after subtracting expenses from income, giving a clear picture of the account balance.

![balance](https://github.com/1uckyswish/financial-ledger/assets/107442415/5f481ab2-879c-4b7f-abc9-c4b2a8176ce6)

## Second Portion Of Program (Ledger Screen)

Upon launching the application, users are greeted with the Ledger screen, offering various options to interact with the Accounting Ledger Application.

![Legder](https://github.com/1uckyswish/financial-ledger/assets/107442415/8806aac4-61b6-4c97-8c4c-bd92bd3efa16)

## Option Show All (A) Display all transaction entries

This option shows all transactions in the account ledger, including both positive and negative amounts.

![All](https://github.com/1uckyswish/financial-ledger/assets/107442415/856ea983-ba40-4c90-96d4-45ccd1395f76)

## Option Deposits Only (D) Display only deposit transactions

This feature presents a comprehensive overview of all positive income deposit transactions for the user.

<img width="1440" alt="deposit" src="https://github.com/1uckyswish/financial-ledger/assets/107442415/854dcf4a-0931-410d-b2e1-2dfdffccf337">

## Option Payments Only (P) Display only negative transactions (payments)

This feature provides a detailed summary of all negative withdrawal transactions for the user.

<img width="1440" alt="payments" src="https://github.com/1uckyswish/financial-ledger/assets/107442415/3d968184-da9f-4d37-b138-4a7fa18cbcfc">

## Option Reports (R)

The reports screen allows users to generate pre-defined reports or perform custom searches. Users can choose from options such as month-to-date, previous month, year-to-date, previous year, and search by vendor.

<img width="1440" alt="Reports" src="https://github.com/1uckyswish/financial-ledger/assets/107442415/abc65621-233b-4ddc-aaae-07e06c42d28f">

## Error Handling Section

###  Home Screen Error handle

If you don't choose one of the options provided in the program, we have a way to help you out. Just make sure to pick a valid option for smooth sailing.

<img width="1440" alt="homescreenerror" src="https://github.com/1uckyswish/financial-ledger/assets/107442415/ac2559c0-b4f0-4982-9c2d-b9a0dda2ee05">

### Ledger Home Screen Error Handling

In case you miss selecting an option on the ledger report screen, we've got you covered. Simply ensure you choose a valid option to keep things running smoothly.

<img width="1440" alt="ledgerHomeerror" src="https://github.com/1uckyswish/financial-ledger/assets/107442415/52eae517-871a-4d25-9aac-bcf291ae2487">


### Continued Program Running Error Handle:

After each action you take within the application, we aim to keep the app running smoothly by prompting you on what to do next. If you ever wish to exit, just type the option, and we'll accommodate your request.

<img width="1440" alt="rerunerror" src="https://github.com/1uckyswish/financial-ledger/assets/107442415/030a03e5-6905-4a21-91d3-3d2015f3bec4">

## Interesting Section of code

This code is interesting because instead of users having to input the date and time themselves, it automatically fetches the current date and time. This is helpful when users want to record a transaction they just made and display it instantly. It also benefits users who might not remember or know the exact date or time, as the feature handles that for them.

![coolcode](https://github.com/1uckyswish/financial-ledger/assets/107442415/44b20598-e46b-4dbb-8768-0e85292cbb8b)

## CSV Output

<img width="1440" alt="Screenshot 2024-04-21 at 11 00 50 PM" src="https://github.com/1uckyswish/financial-ledger/assets/107442415/7aff0836-7490-4166-ae43-60f3f1a0d287">


---

