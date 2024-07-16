package com.pluralsight.data.interfaces;

import com.pluralsight.models.Transaction;

import java.util.List;

/**
 * Interface defining operations for transaction management.
 */
public interface TransactionDao {

    /**
     * Retrieves all transactions associated with a specific user.
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @return A list of Transaction objects representing all transactions for the user.
     */
    List<Transaction> getAllTransactions(int userId);

    /**
     * Creates a new transaction record for a specific user.
     *
     * @param transaction The Transaction object representing the new transaction.
     * @param userId      The ID of the user creating the transaction.
     * @return The Transaction object representing the newly created transaction, or null if creation fails.
     */
    Transaction createTransaction(Transaction transaction, int userId);

    /**
     * Retrieves all deposit transactions (transactions with positive amounts) for a specific user.
     *
     * @param userId The ID of the user whose deposit transactions are to be retrieved.
     * @return A list of Transaction objects representing all deposit transactions for the user.
     */
    List<Transaction> getAllDeposits(int userId);

    /**
     * Retrieves all payment transactions (transactions with negative amounts) for a specific user.
     *
     * @param userId The ID of the user whose payment transactions are to be retrieved.
     * @return A list of Transaction objects representing all payment transactions for the user.
     */
    List<Transaction> getAllPayments(int userId);
}
