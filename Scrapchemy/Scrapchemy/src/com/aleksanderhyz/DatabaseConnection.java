package com.aleksanderhyz;

/**
 *  This class provides methods for connecting to the Scrapchemy_database.db to get data and operate on it in the other classes and to save data to save file database
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    public static final String SCRAPCHEMY_DATABASE_NAME = "Scrapchemy_database.db";

    public static final String CONNECTION_PATH = "jdbc:sqlite:src\\" + SCRAPCHEMY_DATABASE_NAME;

    public static final String MAGICAL_ITEM_TABLE = "Magical Item";
    public static final String MAGICAL_ITEM_ID_COLUMN = "_id";
    public static final int MAGICAL_ITEM_ID_INDEX = 1;


    private Connection connection;


    // opening and closing Scrapchemy database

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_PATH);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }
}
