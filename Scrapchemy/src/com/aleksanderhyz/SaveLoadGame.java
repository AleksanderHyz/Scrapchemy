package com.aleksanderhyz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class providing methods to save and load game progress into .db files
 */

public class SaveLoadGame {

    public static final String SAVE_CONNECTION_PATH = "jdbc:sqlite:src\\saved_games\\";
    // the path is incomplete, it requires file name to be added before opening
        // that operation is performed in open() method

    /* Save game database file structure */

    // player data
    public static final String PLAYER_DATA_TABLE = "Player data";
    public static final String PLAYER_DATA_NAME_COLUMN = "name";                                        // TEXT NOT NULL
    public static final int PLAYER_DATA_NAME_INDEX = 1;
    public static final String PLAYER_DATA_COMMISSIONS_COMPLETED_COLUMN = "commissions completed";      // INTEGER NOT NULL
    public static final int PLAYER_DATA_COMMISSIONS_COMPLETED_INDEX = 2;
    public static final String PLAYER_DATA_WALLET_COLUMN = "wallet";                                    // REAL
    public static final int PLAYER_DATA_WALLET_INDEX = 3;

    // commissions
    public static final String COMMISSIONS_TABLE = "commissions";
    public static final String COMMISSIONS_NUMBER_COLUMN = "number";                                // INTEGER
    public static final int COMMISSIONS_NUMBER_INDEX = 1;
    public static final String COMMISSIONS_PRODUCT_ID_COLUMN = "product id";                        // INTEGER
    public static final int COMMISSIONS_PRODUCT_ID_INDEX = 2;
    public static final String COMMISSIONS_REQUIRED_CURSE_STATUS_COLUMN = "required curse status";  // INTEGER
    public static final int COMMISSIONS_REQUIRED_CURSE_STATUS_INDEX = 3;

    // magical components
    public static final String MAGICAL_COMPONENTS_TABLE = "magical components";
    public static final String MAGICAL_COMPONENTS_COMPONENT_ID_COLUMN = "component id";     // INTEGER
    public static final int MAGICAL_COMPONENTS_COMPONENT_ID_INDEX = 1;
    public static final String MAGICAL_COMPONENTS_MATERIAL_COLUMN = "material";             // INTEGER
    public static final int MAGICAL_COMPONENTS_MATERIAL_INDEX = 2;
    public static final String MAGICAL_COMPONENTS_QUALITY_COLUMN = "quality";               // INTEGER
    public static final int MAGICAL_COMPONENTS_QUALITY_INDEX = 3;
    public static final String MAGICAL_COMPONENTS_CURSED_COLUMN = "cursed";                 // INTEGER
    public static final int MAGICAL_COMPONENTS_CURSED_INDEX = 4;

    // magical items
    public static final String MAGICAL_ITEMS_TABLE = "magical items";
    public static final String MAGICAL_ITEMS_ITEM_ID_COLUMN = "item id";                            // INTEGER
    public static final int MAGICAL_ITEMS_ITEM_ID_INDEX = 1;
    public static final String MAGICAL_ITEMS_CURSED_COLUMN = "cursed";                              // INTEGER
    public static final int MAGICAL_ITEMS_CURSED_INDEX = 2;
    public static final String MAGICAL_ITEMS_COMPONENT1_MATERIAL_COLUMN = "component1 material";    // INTEGER
    public static final int MAGICAL_ITEMS_COMPONENT1_MATERIAL_INDEX = 3;
    public static final String MAGICAL_ITEMS_COMPONENT1_QUALITY_COLUMN = "component1 quality";      // INTEGER
    public static final int MAGICAL_ITEMS_COMPONENT1_QUALITY_INDEX = 4;
    public static final String MAGICAL_ITEMS_COMPONENT2_MATERIAL_COLUMN = "component2 material";    // INTEGER
    public static final int MAGICAL_ITEMS_COMPONENT2_MATERIAL_INDEX = 5;
    public static final String MAGICAL_ITEMS_COMPONENT2_QUALITY_COLUMN = "component2 quality";      // INTEGER
    public static final int MAGICAL_ITEMS_COMPONENT2_QUALITY_INDEX = 6;
    public static final String MAGICAL_ITEMS_COMPONENT3_MATERIAL_COLUMN = "component3 material";    // INTEGER
    public static final int MAGICAL_ITEMS_COMPONENT3_MATERIAL_INDEX = 7;
    public static final String MAGICAL_ITEMS_COMPONENT3_QUALITY_COLUMN = "component3 quality";      // INTEGER
    public static final int MAGICAL_ITEMS_COMPONENT3_QUALITY_INDEX = 8;

    // magical materials
    public static final String MAGICAL_MATERIALS_TABLE = "magical materials";
    public static final String MAGICAL_MATERIALS_MATERIAL_ID_COLUMN = "material id";    // INTEGER
    public static final int MAGICAL_MATERIALS_MATERIAL_ID_INDEX = 1;
    public static final String MAGICAL_MATERIALS_QUALITY_COLUMN = "quality";            // INTEGER
    public static final int MAGICAL_MATERIALS_QUALITY_INDEX = 2;
    public static final String MAGICAL_MATERIALS_MASS_COLUMN = "mass";                  // REAL
    public static final int MAGICAL_MATERIALS_MASS_INDEX = 3;
    public static final String MAGICAL_MATERIALS_CURSED_COLUMN = "cursed";              // INTEGER
    public static final int MAGICAL_MATERIALS_CURSED_INDEX = 4;

    // market
    public static final String MARKET_TABLE = "magical items";
    public static final String MARKET_ITEM_ID_COLUMN = "item id";                            // INTEGER
    public static final int MARKET_ITEM_ID_INDEX = 1;
    public static final String MARKET_CURSED_COLUMN = "cursed";                              // INTEGER
    public static final int MARKET_CURSED_INDEX = 2;
    public static final String MARKET_COMPONENT1_MATERIAL_COLUMN = "component1 material";    // INTEGER
    public static final int MARKET_COMPONENT1_MATERIAL_INDEX = 3;
    public static final String MARKET_COMPONENT1_QUALITY_COLUMN = "component1 quality";      // INTEGER
    public static final int MARKET_COMPONENT1_QUALITY_INDEX = 4;
    public static final String MARKET_COMPONENT2_MATERIAL_COLUMN = "component2 material";    // INTEGER
    public static final int MARKET_COMPONENT2_MATERIAL_INDEX = 5;
    public static final String MARKET_COMPONENT2_QUALITY_COLUMN = "component2 quality";      // INTEGER
    public static final int MARKET_COMPONENT2_QUALITY_INDEX = 6;
    public static final String MARKET_COMPONENT3_MATERIAL_COLUMN = "component3 material";    // INTEGER
    public static final int MARKET_COMPONENT3_MATERIAL_INDEX = 7;
    public static final String MARKET_COMPONENT3_QUALITY_COLUMN = "component3 quality";      // INTEGER
    public static final int MARKET_COMPONENT3_QUALITY_INDEX = 8;


    /* prepared statements: */

    /* saving data: */

    //


    private Connection connection;

    // opening and closing save file

    public boolean open(String playerName) {
        StringBuilder saveFileName = new StringBuilder(playerName + ".db");

        try {
            connection = DriverManager.getConnection(SAVE_CONNECTION_PATH + saveFileName.toString());

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to save file: " + e.getMessage());
            System.out.println(e.getErrorCode());
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

    /* saving data: */

    public enum SaveGameStatus {
        SAVED_SUCCESSFULLY,
        SAVING_FAILED
    }


    // save game
    public SaveGameStatus saveGame (Player player) {
        if (open(player.getName())) {
            try {
                connection.setAutoCommit(false);


            } catch (Exception e0) {
                System.out.println("Saving game exception: " + e0.getMessage());
                try {
                    System.out.println("Performing rollback.");
                    connection.rollback();
                } catch (SQLException e1) {
                    System.out.println("Rollback failed: " + e1.getMessage());
                }
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Couldn't reset autocommit: " + e.getMessage());
                }
            }

            close();
            return SaveGameStatus.SAVED_SUCCESSFULLY;
        } else {
            close();
            return SaveGameStatus.SAVING_FAILED;
        }
    }
}
