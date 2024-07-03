package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/aceBank";

    public static BasicDataSource connect() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(DB_URL);
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        return dataSource;
    }
}
