package com.pluralsight.data;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Database connector
 */
public class DatabaseConnector {

    /**
     * Url of the database
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/aceBank";

    /**
     * Connect to the database
     * @return BasicDataSource
     */
    public static BasicDataSource connect() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(DB_URL);
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        return dataSource;
    }
}
