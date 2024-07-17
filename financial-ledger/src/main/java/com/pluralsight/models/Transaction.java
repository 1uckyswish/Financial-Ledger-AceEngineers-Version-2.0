package com.pluralsight.models;

/**
 * Represents a financial transaction with date, time, description, vendor, and
 * amount.
 */
public class Transaction {
    private String transactionId;
    private String userId;
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    /**
     * Constructors for Transaction
     *
     * @param date
     * @param time
     * @param description
     * @param vendor
     * @param amount
     */
    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // make a transaction
    public Transaction(String transactionId, String userId, String date, String time, String description, String vendor,
                       double amount) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    /**
     * Getter methods
     *
     * @return values from above
     *
     */

    /**
     * Setter methods
     *
     * @void updates values from above
     *
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // getters and setters from db

    @Override
    public String toString() {
        return String.format(
                "+------------+--------------------------+\n" +
                        "| Date       | '%s'\n" +
                        "| Time       | '%s'\n" +
                        "| Description| '%s'\n" +
                        "| Vendor     | '%s'\n" +
                        "| Amount     | $%.2f\n" +
                        "+------------+--------------------------+",
                date, time, description, vendor, amount);
    }

}
