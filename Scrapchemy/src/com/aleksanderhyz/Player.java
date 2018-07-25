package com.aleksanderhyz;

/**
 *  Class that defines the game progress
 */

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;                                      // name of the Player, used for save file name
    private double wallet;                                          // amount of money the Player currently has
    private static final double INITIAL_WALLET = 100_000.00;        // amount of money given to the Player when the game starts
    private int commissionsCompleted;

    // Player's inventory
    private List<MagicalItem> magicalItems;                         // list of Magical Items the Player has bought and hasn't dismantled into components or sold yet
    private List<MagicalItemComponent> magicalComponents;           // list of Magical Item Components the Player has salvaged from Magical Items and hasn't processed into Magical Materials or sold yet
    private List<MagicalMaterial> magicalMaterials;                 // list of Magical Materials the Player has made from Magical Item Components hasn't used to make Magical Products or sold yet

    // Player's market
    private List<MagicalProduct> commissionList;                    // list of Magical Product commissions available for the Player to accomplish
    private List<MagicalItem> market;                               // list of Magical Items that are currently available for the Player to buy at the Market

    public Player(String name) {
        this.name = name;
        this.commissionsCompleted = 0;
        this.wallet = INITIAL_WALLET;
        this.magicalItems = new ArrayList<>();
        this.magicalComponents = new ArrayList<>();
        this.magicalMaterials = new ArrayList<>();
        this.commissionList = new ArrayList<>();
        this.market = new ArrayList<>();
    }

    public String getName() {
        return name;
    }



    // wallet operations

    public enum TransactionType {
        BUYING,
        SELLING
    }

    public enum TransactionStatus {
        BOUGHT_SUCCESSFULLY,
        SOLD_SUCCESSFULLY,
        ITEM_NOT_AVAILABLE,
        NOT_ENOUGH_MONEY
    }

    public double getWallet() {
        return wallet;
    }

    public boolean updateWallet (double amount, TransactionType transactionType) {
        // method used both for buying and selling magical objects
        if (amount >= 0) { //checking if price is negative number
            if (transactionType.equals(TransactionType.SELLING)) {
                this.wallet += amount;
                return true;
                // magical object sold successfully
            } else if (transactionType.equals(TransactionType.BUYING)) {
                if (this.wallet >= amount) {
                    this.wallet -= amount;
                    return true;
                    // magical object bought successfully
                } else {
                    return false;
                    // player has no enough money
                }
            } else {
                return false;
                // in case of wrong transaction type
            }
        } else {
            return false;
            // price was a negative number
        }
    }


    // market operations:

    public List<MagicalItem> getMarket() {
        return market;
    }


    // inventory operations:

    public List<MagicalItem> getMagicalItems() {
        return magicalItems;
    }

    public List<MagicalItemComponent> getMagicalComponents() {
        return magicalComponents;
    }

    public List<MagicalMaterial> getMagicalMaterials() {
        return magicalMaterials;
    }
}