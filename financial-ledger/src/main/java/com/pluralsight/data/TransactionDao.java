package com.pluralsight.data;

import com.pluralsight.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getAllTransactions(int userId);
    List<Transaction> getAllDeposits(int userId);
    List<Transaction> getAllPayments(int userId);
}
