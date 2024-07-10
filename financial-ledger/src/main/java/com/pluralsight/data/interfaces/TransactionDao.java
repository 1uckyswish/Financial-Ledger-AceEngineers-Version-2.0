package com.pluralsight.data.interfaces;

import com.pluralsight.models.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getAllTransactions(int userId);
    Transaction createTransaction(Transaction transaction, int userId);
    List<Transaction> getAllDeposits(int userId);
    List<Transaction> getAllPayments(int userId);
}
