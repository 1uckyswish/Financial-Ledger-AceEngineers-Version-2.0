package com.pluralsight.data;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Utility class for connecting to the MySQL database.
 */
public class DatabaseConnector {

    /**
     * URL of the MySQL database.
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/aceBank";

    /**
     * Connects to the MySQL database using DBCP BasicDataSource.
     *
     * @return BasicDataSource object configured with database connection details.
     */
    public static BasicDataSource connect() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(DB_URL);
        dataSource.setUsername("root"); // Replace with actual database username
        dataSource.setPassword("password"); // Replace with actual database password
        return dataSource;
    }
}
