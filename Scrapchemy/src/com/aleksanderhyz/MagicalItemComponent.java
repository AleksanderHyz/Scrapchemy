package com.aleksanderhyz;

import java.util.ArrayList;
import java.util.List;

/**
 *  Components are parts the Magical Items are made of
 */

public class MagicalItemComponent extends MagicalObject {

    private double basePrice;
    private double mass;
    private String materialID;
    private String materialName;


    @Override
    void calculatePrice() {
        double Q = this.quality.getValue();
        switch (this.quality.getVisibleQuality()) {
            case UNACCEPTABLE_QUALITY:
                this.price = 0.00;
                break;
            case LOW_QUALITY:
                this.price = this.basePrice * (Q/10);
                break;
            case MODERATE_QUALITY:
                this.price = basePrice * (Q/6);
                break;
            case HIGH_QUALITY:
                this.price = basePrice * (Q/2);
                break;
            case EXCELLENT_QUALITY:
                this.price = basePrice * Q;
                break;
        }
    }

    // Player selling the component they have in their inventory
    @Override
    public Player.TransactionStatus sell(Player player) {
        if (player.getMagicalComponents().contains(this)) {
            if(player.updateWallet((this.price * SELLING_MODIFIER), Player.TransactionType.SELLING)) {
                player.getMagicalComponents().remove(this);
                return Player.TransactionStatus.SOLD_SUCCESSFULLY;
            }
            return null;
        } else {
            return Player.TransactionStatus.OBJECT_NOT_AVAILABLE;
        }
    }

    protected MagicalItemComponent(String name, double price, boolean cursed, Quality quality, double basePrice, double mass, String materialID) {
        super(name, price, cursed, quality);
        this.basePrice = basePrice;
        this.mass = mass;
        this.materialID = materialID;
    }

    // method to turn a Component which was a part of an Item into a standalone object
    protected MagicalItemComponent salvageMagicalItemComponent () {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.open();
        this.materialName = (String) databaseConnection.getMagicalMaterialByID(this.materialID).get(1);
        // 0 - _id, 1 - name, 2 - group _id, 3 - base price
        databaseConnection.close();
        return this;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getMass() {
        return mass;
    }

    public String getMaterialID() {
        return materialID;
    }

    // processing Component into Magical Material:
    public MagicalMaterial processMagicalItemComponent (Player player) {

        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.open();

        List<Object> materialFields = new ArrayList<>(databaseConnection.getMagicalMaterialByID(this.materialID));
        // 0 - material _id, 1 - material name, 2 - material group, 3 - base price

        //String name, double price, boolean cursed, Quality quality, String id, double basePrice, String materialGroup, double mass
        String materialID = (String) materialFields.get(0);
        String materialName = (String) materialFields.get(1);
        String materialGroup = (String) materialFields.get(2); // id
        double materialBasePrice = (Double) materialFields.get(3);
        boolean materialCursed = this.cursed;
        Quality materialQuality = this.quality;
        double materialMass = this.mass;
        MagicalMaterial magicalMaterial = new MagicalMaterial(materialName, 0, materialCursed, materialQuality, materialID, materialBasePrice, materialGroup, materialMass);
        magicalMaterial.calculatePrice();

        databaseConnection.close();

        return magicalMaterial;
    }
}
