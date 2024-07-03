package com.pluralsight;

/**
 * Represents a financial transaction with date, time, description, vendor, and amount.
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
     * Constructs a transaction with the given details.
     * @param date        The date of the transaction
     * @param time        The time of the transaction
     * @param description The description of the transaction
     * @param vendor      The vendor involved in the transaction
     * @param amount      The amount of money involved in the transaction
     */
    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public Transaction(String transactionId, String userId, String date, String time, String description, String vendor, double amount) {
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
     * @return values from above
     *
     */

    /**
     * Setter methods
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
                        "| Amount     | $%s\n" +
                        "+------------+--------------------------+",
                date, time, description, vendor, amount);
    }



}
