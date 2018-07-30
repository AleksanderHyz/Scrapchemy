package com.aleksanderhyz;

/**
 *  This class provides methods for connecting to the Scrapchemy_database.db to get data and operate on it in the other classes and to save data to save file database
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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



    /* prepared statements: */
    // count rows
//    public static final String COUNT_ROWS = "SELECT COUNT(*) AS count FROM [?]";
//    private PreparedStatement countRows;

    // get Magical Materials from a Group
    public static final String GET_MATERIALS_FROM_GROUP =
            "SELECT " + MAGICAL_MATERIAL_ID_COLUMN +
            " FROM [" + MAGICAL_MATERIAL_TABLE +
            "] WHERE [" + MAGICAL_MATERIAL_GROUP_COLUMN + "] = ?";
    private PreparedStatement getMaterialsFromGroup;

    // get Magical Material by _id:
    public static final String GET_MAGICAL_MATERIAL_BY_ID =
                    "SELECT " + "*" +
                    " FROM [" + MAGICAL_MATERIAL_TABLE +
                    "] WHERE [" + MAGICAL_MATERIAL_ID_COLUMN + "] = ?";
    private PreparedStatement getMagicalMaterialByID;

    // get Magical Item from table by _id:
    public static final String GET_MAGICAL_ITEM_BY_ID =
            "SELECT " + "*" +
            " FROM [" + MAGICAL_ITEM_TABLE +
            "] WHERE " + MAGICAL_ITEM_ID_COLUMN + " = ?";
    private PreparedStatement getMagicalItemByID;

    // get Magical Item Component from table by _id:
    public static final String GET_MAGICAL_ITEM_COMPONENT_BY_ID =
            "SELECT " + "*" +
            " FROM [" + MAGICAL_ITEM_COMPONENT_TABLE +
            "] WHERE " + MAGICAL_ITEM_COMPONENT_ID_COLUMN + " = ?";
    private PreparedStatement getMagicalItemComponentByID;


    private Connection connection;


    // opening and closing Scrapchemy database

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_PATH);
//            countRows = connection.prepareStatement(COUNT_ROWS);
            // countRows was causing weird errors, so it's performed in count() without using a prepared statement
            getMaterialsFromGroup = connection.prepareStatement(GET_MATERIALS_FROM_GROUP);
            getMagicalMaterialByID = connection.prepareStatement(GET_MAGICAL_MATERIAL_BY_ID);
            getMagicalItemByID = connection.prepareStatement(GET_MAGICAL_ITEM_BY_ID);
            getMagicalItemComponentByID = connection.prepareStatement(GET_MAGICAL_ITEM_COMPONENT_BY_ID);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            System.out.println(e.getErrorCode());
            return false;
        }
    }

    public void close() {
        try {
//            if (countRows != null) {
//                countRows.close();
//            }

            if (getMaterialsFromGroup != null) {
                getMaterialsFromGroup.close();
            }

            if (getMagicalMaterialByID != null) {
                getMagicalMaterialByID.close();
            }

            if (getMagicalItemByID != null) {
                getMagicalItemByID.close();
            }

            if (getMagicalItemComponentByID != null) {
                getMagicalItemComponentByID.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    // method to count rows of a table, needed when rolling Magical Items
    protected int count(String table_name) {
        String sqlCode = "SELECT COUNT(*) AS count FROM [" + table_name +"]";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCode)){

            int count = resultSet.getInt("count");
            return count;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    // method to create array of IDs of material from specified material group
    protected List<String> getMaterialsFromGroup (String groupID) {
        List<String> items = new ArrayList<>();
        try{
            getMaterialsFromGroup.setString(1, groupID);
            ResultSet resultSet = getMaterialsFromGroup.executeQuery();
            while (resultSet.next()) {
                items.add(resultSet.getString(MAGICAL_MATERIAL_ID_INDEX));
            }

            return items;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    // getting Magical Material by the _id
    protected List<Object> getMagicalMaterialByID(String materialID) {
        List<Object> fields = new ArrayList<>();
        try {
            getMagicalMaterialByID.setString(1, materialID);
            ResultSet resultSet = getMagicalMaterialByID.executeQuery();
            while (resultSet.next()) {
                fields.add(resultSet.getString(MAGICAL_MATERIAL_ID_INDEX));
                fields.add(resultSet.getString(MAGICAL_MATERIAL_NAME_INDEX));
                fields.add(resultSet.getString(MAGICAL_MATERIAL_GROUP_INDEX));
                fields.add(resultSet.getDouble(MAGICAL_MATERIAL_BASE_PRICE_INDEX));
            }
            return fields;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    // get Magical Item fields by _id
    protected List<String> getMagicalItemByID (String itemID) {
        List<String> fields = new ArrayList<>();
        try {
            getMagicalItemByID.setString(1, itemID);
            ResultSet resultSet = getMagicalItemByID.executeQuery();
            while (resultSet.next()) {
                fields.add(resultSet.getString(MAGICAL_ITEM_NAME_INDEX));
                fields.add(resultSet.getString(MAGICAL_ITEM_COMPONENT1_INDEX));
                fields.add(resultSet.getString(MAGICAL_ITEM_COMPONENT2_INDEX));
                fields.add(resultSet.getString(MAGICAL_ITEM_COMPONENT3_INDEX));
            }
            return fields;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    // get Magical Item Component fields by _id
    protected List<Object> getMagicalItemComponentsByID (String itemComponentID) {
        List<Object> fields = new ArrayList<>();
        try {
            getMagicalItemComponentByID.setString(1, itemComponentID);
            ResultSet resultSet = getMagicalItemComponentByID.executeQuery();
            while (resultSet.next()) {
                fields.add(resultSet.getString(MAGICAL_ITEM_COMPONENT_ID_INDEX));
                fields.add(resultSet.getString(MAGICAL_ITEM_COMPONENT_NAME_INDEX));
                fields.add(resultSet.getString(MAGICAL_ITEM_COMPONENT_MATERIAL_GROUP_INDEX));
                fields.add(resultSet.getDouble(MAGICAL_ITEM_COMPONENT_MASS_INDEX));
                fields.add(resultSet.getDouble(MAGICAL_ITEM_COMPONENT_BASE_PRICE_INDEX));
            }
            return fields;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
}
