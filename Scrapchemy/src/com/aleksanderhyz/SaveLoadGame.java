package com.aleksanderhyz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class providing methods to save and load game progress into .db files
 */

public class SaveLoadGame {

    public static final String SAVE_CONNECTION_PATH = "jdbc:sqlite:src\\saved_games\\";
    // the path is incomplete, it requires file name to be added before opening
        // that operation is performed in open() method

    /* Save game database file structure */

    // player data
    public static final String PLAYER_DATA_TABLE = "player data";
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
    public static final String MARKET_TABLE = "market";
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

    // insert Player data
    private static final String INSERT_PLAYER_DATA =
            "INSERT INTO [" + PLAYER_DATA_TABLE + "] " +
                    "([" + PLAYER_DATA_NAME_COLUMN + "], " +
                    "[" + PLAYER_DATA_COMMISSIONS_COMPLETED_COLUMN + "], " +
                    "[" + PLAYER_DATA_WALLET_COLUMN + "]) " +
                    "VALUES (?, ?, ?)";
    private PreparedStatement insertPlayerData;

    // insert commission
    private static final String INSERT_COMMISSION =
            "INSERT INTO [" + COMMISSIONS_TABLE + "] " +
                    "([" + COMMISSIONS_NUMBER_COLUMN + "], " +
                    "[" + COMMISSIONS_PRODUCT_ID_COLUMN + "], " +
                    "[" + COMMISSIONS_REQUIRED_CURSE_STATUS_COLUMN + "]) " +
                    "VALUES (?, ?, ?)";
    private PreparedStatement insertCommission;

    // insert magical component
    private static final String INSERT_COMPONENT =
            "INSERT INTO [" + MAGICAL_COMPONENTS_TABLE + "] " +
                    "([" + MAGICAL_COMPONENTS_COMPONENT_ID_COLUMN + "], " +
                    "[" + MAGICAL_COMPONENTS_MATERIAL_COLUMN + "], " +
                    "[" + MAGICAL_COMPONENTS_QUALITY_COLUMN + "], " +
                    "[" + MAGICAL_COMPONENTS_CURSED_COLUMN + "]) " +
                    "VALUES (?, ?, ?, ?)";
    private PreparedStatement insertComponent;

    // insert magical item
    private static final String INSERT_ITEM =
            "INSERT INTO [" + MAGICAL_ITEMS_TABLE + "] " +
                    "([" + MAGICAL_ITEMS_ITEM_ID_COLUMN + "], " +
                    "[" + MAGICAL_ITEMS_CURSED_COLUMN + "], " +
                    "[" + MAGICAL_ITEMS_COMPONENT1_MATERIAL_COLUMN + "], " +
                    "[" + MAGICAL_ITEMS_COMPONENT1_QUALITY_COLUMN + "], " +
                    "[" + MAGICAL_ITEMS_COMPONENT2_MATERIAL_COLUMN + "], " +
                    "[" + MAGICAL_ITEMS_COMPONENT2_QUALITY_COLUMN + "], " +
                    "[" + MAGICAL_ITEMS_COMPONENT3_MATERIAL_COLUMN + "], " +
                    "[" + MAGICAL_ITEMS_COMPONENT3_QUALITY_COLUMN + "]) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement insertItem;

    // insert magical material
    private static final String INSERT_MATERIAL =
            "INSERT INTO [" + MAGICAL_MATERIALS_TABLE + "] " +
                    "([" + MAGICAL_MATERIALS_MATERIAL_ID_COLUMN + "], " +
                    "[" + MAGICAL_MATERIALS_QUALITY_COLUMN + "], " +
                    "[" + MAGICAL_MATERIALS_MASS_COLUMN + "], " +
                    "[" + MAGICAL_MATERIALS_CURSED_COLUMN + "]) " +
                    "VALUES (?, ?, ?, ?)";
    private PreparedStatement insertMaterial;

    // insert item from market
    private static final String INSERT_MARKET_ITEM =
            "INSERT INTO [" + MARKET_TABLE + "] " +
                    "([" + MARKET_ITEM_ID_COLUMN + "], " +
                    "[" + MARKET_CURSED_COLUMN + "], " +
                    "[" + MARKET_COMPONENT1_MATERIAL_COLUMN + "], " +
                    "[" + MARKET_COMPONENT1_QUALITY_COLUMN + "], " +
                    "[" + MARKET_COMPONENT2_MATERIAL_COLUMN + "], " +
                    "[" + MARKET_COMPONENT2_QUALITY_COLUMN + "], " +
                    "[" + MARKET_COMPONENT3_MATERIAL_COLUMN + "], " +
                    "[" + MARKET_COMPONENT3_QUALITY_COLUMN + "]) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement insertMarketItem;

    private Connection connection;

    // opening and closing save file

    public boolean open(String playerName) {
        String saveFileName = playerName + ".db";

        try {
            connection = DriverManager.getConnection(SAVE_CONNECTION_PATH + saveFileName);

            // create the tables ( with "IF NOT EXISTS" clause):
            createSaveFileTables();

            insertPlayerData = connection.prepareStatement(INSERT_PLAYER_DATA);
            insertCommission = connection.prepareStatement(INSERT_COMMISSION);
            insertComponent = connection.prepareStatement(INSERT_COMPONENT);
            insertItem = connection.prepareStatement(INSERT_ITEM);
            insertMaterial = connection.prepareStatement(INSERT_MATERIAL);
            insertMarketItem = connection.prepareStatement(INSERT_MARKET_ITEM);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to save file: " + e.getMessage());
            System.out.println(e.getErrorCode());
            return false;
        }
    }

    public void close() {
        try {
            if (insertPlayerData != null) {
                insertPlayerData.close();
            }
            if (insertCommission != null) {
                insertCommission.close();
            }
            if (insertComponent != null) {
                insertComponent.close();
            }
            if (insertItem != null) {
                insertItem.close();
            }
            if (insertMaterial != null) {
                insertMaterial.close();
            }
            if (insertMarketItem != null) {
                insertMarketItem.close();
            }

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
        // SaveGameStatus is defined at the start, as SAVING_FAILED
        // and returned only at the end of this method
        // if saving game will complete successfully it will be turned into SAVED_SUCCESSFULLY before being returned
        SaveGameStatus saveGameStatus = SaveGameStatus.SAVING_FAILED;
        if (open(player.getName())) {
            try {
                // set Auto Commit to false to perform rollback in case error occurs in the middle of saving data
                connection.setAutoCommit(false);

                if (
                        // fill the tables with records:
                        savePlayerData(player) &&
                        saveCommissions(player) &&
                        saveMagicalComponents(player) &&
                        saveMagicalItems(player) &&
                        saveMagicalMaterial(player) &&
                        saveMarket(player)
                        ) {
                    saveGameStatus = SaveGameStatus.SAVED_SUCCESSFULLY;
                }

            } catch (Exception e0) {
                // catching any kind of exception that caused saving game to fail
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
        }

        close();

        return saveGameStatus;
    }

    private boolean createSaveFileTables() {

        boolean success = true;

        String createPlayerDataTable = "CREATE TABLE IF NOT EXISTS \"" + PLAYER_DATA_TABLE + "\" " +
                "( `" + PLAYER_DATA_NAME_COLUMN + "` TEXT NOT NULL, " +
                "`" + PLAYER_DATA_COMMISSIONS_COMPLETED_COLUMN + "` INTEGER NOT NULL, " +
                "`" + PLAYER_DATA_WALLET_COLUMN + "` REAL )";
        String createCommissionsTable = "CREATE TABLE IF NOT EXISTS \"commissions\" " +
                "( `number` INTEGER NOT NULL, " +
                "`product id` INTEGER NOT NULL, " +
                "`required curse status` INTEGER NOT NULL )";
        String createMagicalComponentsTable = "CREATE TABLE IF NOT EXISTS \"magical components\" " +
                "( `component id` INTEGER NOT NULL, " +
                "`material` INTEGER, " +
                "`quality` INTEGER, " +
                "`cursed` INTEGER )";
        String createMagicalItemsTable = "CREATE TABLE IF NOT EXISTS \"magical items\" " +
                "( `item id` INTEGER NOT NULL, " +
                "`cursed` INTEGER, " +
                "`component1 material` INTEGER, " +
                "`component1 quality` INTEGER, " +
                "`component2 material` INTEGER, " +
                "`component2 quality` INTEGER, " +
                "`component3 material` INTEGER, " +
                "`component3 quality` INTEGER )";
        String createMagicalMaterialsTable = "CREATE TABLE IF NOT EXISTS \"magical materials\" " +
                "( `material id` INTEGER NOT NULL, " +
                "`quality` INTEGER, " +
                "`mass` REAL, " +
                "`cursed` INTEGER )";
        String createMarketTable = "CREATE TABLE IF NOT EXISTS \"market\" " +
                "( `item id` INTEGER NOT NULL, " +
                "`cursed` INTEGER, " +
                "`component1 material` INTEGER, " +
                "`component1 quality` INTEGER, " +
                "`component2 material` INTEGER, " +
                "`component2 quality` INTEGER, " +
                "`component3 material` INTEGER, " +
                "`component3 quality` INTEGER )";

        try {
            Statement statement = connection.createStatement();
            statement.execute(createPlayerDataTable);
            statement.execute(createCommissionsTable);
            statement.execute(createMagicalComponentsTable);
            statement.execute(createMagicalItemsTable);
            statement.execute(createMagicalMaterialsTable);
            statement.execute(createMarketTable);

        } catch (SQLException e) {
            success = false;
            System.out.println("Couldn't create the tables: " + e.getMessage());
        }

        return success;
    }

    private boolean savePlayerData(Player player) {

        boolean success = true;

        try {

            String name = player.getName();
            int commissionsCompleted = player.getCommissionsCompleted();
            double wallet = player.getWallet();

            insertPlayerData.setString(1, name);
            insertPlayerData.setInt(2, commissionsCompleted);
            insertPlayerData.setDouble(3, wallet);

            insertPlayerData.executeUpdate();

        } catch (SQLException e) {
            success = false;
            System.out.println("Couldn't save Player data: " + e.getMessage());
        }

        return success;
    }

    private boolean saveCommissions(Player player) {

        boolean success = true;

        try {
            List<MagicalProduct> commissions = new ArrayList<>(player.getCommissionList());
            for (MagicalProduct commission : commissions) {

                int number = commission.getCommissionNumber();
                int productID = Integer.parseInt(commission.getId());
                // required curse status is kept in .db file as integer,
                // with values corresponding to RequiredCurseStatus values as:
                // 0 - CURSED
                // 1 - CLEAN
                // 2 - NO_MATTER
                int requiredCurseStatus = 2;

                if (commission.getRequiredCurseStatus().equals(MagicalProduct.RequiredCurseStatus.CURSED)) {
                    requiredCurseStatus = 0;
                } else if (commission.getRequiredCurseStatus().equals(MagicalProduct.RequiredCurseStatus.CLEAN)) {
                    requiredCurseStatus = 1;
                } // else, it stays as 2

                insertCommission.setInt(1, number);
                insertCommission.setInt(2, productID);
                insertCommission.setInt(3, requiredCurseStatus);

                insertCommission.executeUpdate();
            }
        } catch (SQLException e) {
            success = false;
            System.out.println("Couldn't save commissions: " + e.getMessage());
        }

        return success;
    }

    private boolean saveMagicalComponents(Player player) {

        boolean success = true;

        try {
            List<MagicalItemComponent> magicalItemComponents = new ArrayList<>(player.getMagicalComponents());
            for (MagicalItemComponent component : magicalItemComponents) {

                int componentID = Integer.parseInt(component.getId());
                int material = Integer.parseInt(component.getMaterialID());
                int quality = component.getQuality().getValue();
                int cursed = (component.isCursed()) ? 1 : 0;

                insertComponent.setInt(1, componentID);
                insertComponent.setInt(2, material);
                insertComponent.setInt(3, quality);
                insertComponent.setInt(4, cursed);

                insertComponent.executeUpdate();
            }
        } catch (SQLException e) {
            success = false;
            System.out.println("Couldn't save components: " + e.getMessage());
        }

        return success;
    }

    private boolean saveMagicalItems(Player player) {

        boolean success = true;

        try {
            List<MagicalItem> magicalItems = new ArrayList<>(player.getMagicalItems());
            for (MagicalItem magicalItem : magicalItems) {

                int itemID = Integer.parseInt(magicalItem.getId());
                int cursed = (magicalItem.isCursed()) ? 1 : 0;

                insertItem.setInt(1, itemID);
                insertItem.setInt(2, cursed);

                int i = 3;
                for (MagicalItemComponent component : magicalItem.getComponents()) {
                    // 3 - component1 material index
                    // 4 - component1 quality index
                    // 5 - component2 material index
                    // 6 - component2 quality index
                    // 7 - component3 material index
                    // 8 - component3 quality index
                    int componentMaterial = Integer.parseInt(component.getMaterialID());
                    int componentQuality = component.getQuality().getValue();

                    insertItem.setInt(i, componentMaterial);
                    insertItem.setInt(i+1, componentQuality);

                    i += 2;
                }

                insertItem.executeUpdate();
            }
        } catch (SQLException e) {
            success = false;
            System.out.println("Couldn't save items: " + e.getMessage());
        }

        return success;
    }

    private boolean saveMagicalMaterial(Player player) {

        boolean success = true;

        try {
            List<MagicalMaterial> magicalMaterials = new ArrayList<>(player.getMagicalMaterials());
            for (MagicalMaterial magicalMaterial : magicalMaterials) {

                int materialID = Integer.parseInt(magicalMaterial.getId());
                int materialQuality = magicalMaterial.getQuality().getValue();
                double materialMass = magicalMaterial.getMass();
                int cursed = (magicalMaterial.isCursed()) ? 1 : 0;

                insertMaterial.setInt(1, materialID);
                insertMaterial.setInt(2, materialQuality);
                insertMaterial.setDouble(3, materialMass);
                insertMaterial.setInt(4, cursed);

                insertMaterial.executeUpdate();
            }

        } catch (SQLException e) {
            success = false;
            System.out.println("Couldn't save materials: " + e.getMessage());
        }

        return success;
    }

    private boolean saveMarket(Player player) {

        boolean success = true;

        try {
            List<MagicalItem> marketItems = new ArrayList<>(player.getMarket());
            for (MagicalItem marketItem : marketItems) {

                int itemID = Integer.parseInt(marketItem.getId());
                int cursed = (marketItem.isCursed()) ? 1 : 0;

                insertMarketItem.setInt(1, itemID);
                insertMarketItem.setInt(2, cursed);

                int i = 3;
                for (MagicalItemComponent component : marketItem.getComponents()) {
                    // 3 - component1 material index
                    // 4 - component1 quality index
                    // 5 - component2 material index
                    // 6 - component2 quality index
                    // 7 - component3 material index
                    // 8 - component3 quality index
                    int componentMaterial = Integer.parseInt(component.getMaterialID());
                    int componentQuality = component.getQuality().getValue();

                    insertMarketItem.setInt(i, componentMaterial);
                    insertMarketItem.setInt(i+1, componentQuality);

                    i += 2;
                }

                insertMarketItem.executeUpdate();

            }
        } catch (SQLException e) {
            success = false;
            System.out.println("Couldn't save market: " + e.getMessage());
        }

        return success;
    }
}
