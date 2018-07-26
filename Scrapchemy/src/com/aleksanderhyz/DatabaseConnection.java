package com.aleksanderhyz;

/**
 *  This class provides methods for connecting to the Scrapchemy_database.db to get data and operate on it in the other classes and to save data to save file database
 */

import java.sql.*;

public class DatabaseConnection {

    public static final String SCRAPCHEMY_DATABASE_NAME = "Scrapchemy_database.db";

    public static final String CONNECTION_PATH = "jdbc:sqlite:src\\" + SCRAPCHEMY_DATABASE_NAME;


    /* Scrapchemy database structure */

    // Magical Item table names and indexes:
    public static final String MAGICAL_ITEM_TABLE = "Magical Item";
    public static final String MAGICAL_ITEM_ID_COLUMN = "_id";                  // INTEGER PRIMARY KEY
    public static final int MAGICAL_ITEM_ID_INDEX = 1;
    public static final String MAGICAL_ITEM_NAME_COLUMN = "name";               // TEXT NOT NULL
    public static final int MAGICAL_ITEM_NAME_INDEX = 2;
    public static final String MAGICAL_ITEM_COMPONENT1_COLUMN = "component1";   // INTEGER (Magical Item Component _id)
    public static final int MAGICAL_ITEM_COMPONENT1_INDEX = 3;
    public static final String MAGICAL_ITEM_COMPONENT2_COLUMN = "component2";   // INTEGER (Magical Item Component _id)
    public static final int MAGICAL_ITEM_COMPONENT2_INDEX = 4;
    public static final String MAGICAL_ITEM_COMPONENT3_COLUMN = "component3";   // INTEGER (Magical Item Component _id)
    public static final int MAGICAL_ITEM_COMPONENT3_INDEX = 5;

    // Magical Item Component table names and indexes:
    public static final String MAGICAL_ITEM_COMPONENT_TABLE = "Magical Item Component";
    public static final String MAGICAL_ITEM_COMPONENT_ID_COLUMN = "_id";                        // INTEGER PRIMARY KEY
    public static final int MAGICAL_ITEM_COMPONENT_ID_INDEX = 1;
    public static final String MAGICAL_ITEM_COMPONENT_NAME_COLUMN = "name";                     // TEXT NOT NULL
    public static final int MAGICAL_ITEM_COMPONENT_NAME_INDEX = 2;
    public static final String MAGICAL_ITEM_COMPONENT_MATERIAL_GROUP_COLUMN = "material group"; // INTEGER (Magical Material Group _id)
    public static final int MAGICAL_ITEM_COMPONENT_MATERIAL_GROUP_INDEX = 3;
    public static final String MAGICAL_ITEM_COMPONENT_MASS_COLUMN = "mass";                     // REAL
    public static final int MAGICAL_ITEM_COMPONENT_MASS_INDEX = 4;
    public static final String MAGICAL_ITEM_COMPONENT_BASE_PRICE_COLUMN = "base price";         // REAL
    public static final int MAGICAL_ITEM_COMPONENT_BASE_PRICE_INDEX = 5;

    // Magical Material Group table names and indexes:
    public static final String MAGICAL_MATERIAL_GROUP_TABLE = "Magical Material Group";
    public static final String MAGICAL_MATERIAL_GROUP_ID_COLUMN = "_id";                // INTEGER PRIMARY KEY
    public static final int MAGICAL_MATERIAL_GROUP_ID_INDEX = 1;
    public static final String MAGICAL_MATERIAL_GROUP_NAME_COLUMN = "name";             // TEXT NOT NULL
    public static final int MAGICAL_MATERIAL_GROUP_NAME_INDEX = 2;

    // Magical Material table names and indexes:
    public static final String MAGICAL_MATERIAL_TABLE = "Magical Material";
    public static final String MAGICAL_MATERIAL_ID_COLUMN = "_id";                  // INTEGER PRIMARY KEY
    public static final int MAGICAL_MATERIAL_ID_INDEX = 1;
    public static final String MAGICAL_MATERIAL_NAME_COLUMN = "name";               // TEXT NOT NULL
    public static final int MAGICAL_MATERIAL_NAME_INDEX = 2;
    public static final String MAGICAL_MATERIAL_GROUP_COLUMN = "group";             // INTEGER (Magical Material Group _id)
    public static final int MAGICAL_MATERIAL_GROUP_INDEX = 3;
    public static final String MAGICAL_MATERIAL_BASE_PRICE_COLUMN = "base price";   // REAL
    public static final int MAGICAL_MATERIAL_BASE_PRICE_INDEX = 4;

    // Magical Product table names and indexes:
    public static final String MAGICAL_PRODUCT_TABLE = "Magical Product";
    public static final String MAGICAL_PRODUCT_ID_COLUMN = "_id";                           // INTEGER PRIMARY KEY
    public static final int MAGICAL_PRODUCT_ID_INDEX = 1;
    public static final String MAGICAL_PRODUCT_NAME_COLUMN = "name";                        // TEXT NOT NULL
    public static final int MAGICAL_PRODUCT_NAME_INDEX = 2;
    public static final String MAGICAL_PRODUCT_FUEL_MASS_COLUMN = "fuel mass";              // REAL
    public static final int MAGICAL_PRODUCT_FUEL_MASS_INDEX = 3;
    public static final String MAGICAL_PRODUCT_MATERIAL1_COLUMN = "material1";              // INTEGER (Magical Material _id)
    public static final int MAGICAL_PRODUCT_MATERIAL1_INDEX = 4;
    public static final String MAGICAL_PRODUCT_MATERIAL1_MASS_COLUMN = "material1 mass";    // REAL
    public static final int MAGICAL_PRODUCT_MATERIAL1_MASS_INDEX = 5;
    public static final String MAGICAL_PRODUCT_MATERIAL2_COLUMN = "material2";              // INTEGER (Magical Material _id)
    public static final int MAGICAL_PRODUCT_MATERIAL2_INDEX = 6;
    public static final String MAGICAL_PRODUCT_MATERIAL2_MASS_COLUMN = "material2 mass";    // REAL
    public static final int MAGICAL_PRODUCT_MATERIAL2_MASS_INDEX = 7;
    public static final String MAGICAL_PRODUCT_MATERIAL3_COLUMN = "material3";              // INTEGER (Magical Material _id)
    public static final int MAGICAL_PRODUCT_MATERIAL3_INDEX = 8;
    public static final String MAGICAL_PRODUCT_MATERIAL3_MASS_COLUMN = "material3 mass";    // REAL
    public static final int MAGICAL_PRODUCT_MATERIAL3_MASS_INDEX = 9;



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

    // method to count rows of a table, needed when rolling Magical Items
    public int count(String table_name) {
        String sql_code = "SELECT COUNT(*) AS count FROM [" + table_name +"]";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_code)){
            int count = resultSet.getInt("count");
            return count;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }


}
