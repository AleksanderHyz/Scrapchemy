package com.aleksanderhyz;

import java.util.ArrayList;
import java.util.List;

/**
 *  Materials salvaged from Magical Item Components
 *  for selling or using to create commissioned Magical Products
 */

public class MagicalMaterial extends MagicalObject {

    // id's of material groups that can be treated as fuel for making Magical Product
    public static final String WOOD_GROUP_ID = "1";
        // as for now the program is working in a way where Wood group is the only accepted fuel
        // and every place a fuel is required the material is checked for being in Wood group
        // also the program works on assumption that the used database has Wood group under the "1" _id
        //
        // if that was to be changed (as in more material groups to be accepted as fuel)
        // it would be required to change the code in every place WOOD_GROUP_ID constant is called
        // and editing the database to have a field for Material Groups determining if it's a fuel or not
        // as for now it's left like this because there are no plans to introduce any new fuel materials

    private String id;
    private double basePrice;
    private String materialGroup;
    private double mass;


    // calculating price for a kilogram of the material
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

//    // sell selected amount of material
//    public Player.TransactionStatus sellSome(Player player, double amount) {
//        return null;
//    }
    // let's leave that the player can sell only whole batch of the material they have at once or nothing

    // selling whole Magical Material from the inventory
    @Override
    public Player.TransactionStatus sell(Player player) {
        if (player.getMagicalMaterials().contains(this)) {
            if(player.updateWallet((this.price * SELLING_MODIFIER), Player.TransactionType.SELLING)) {
                player.getMagicalMaterials().remove(this);
                return Player.TransactionStatus.SOLD_SUCCESSFULLY;
            }
            return null;
        } else {
            return Player.TransactionStatus.OBJECT_NOT_AVAILABLE;
        }
    }

    // creating Magical Material from a Magical Item Component
    public MagicalMaterial(String name, double price, boolean cursed, Quality quality, String id, double basePrice, String materialGroup, double mass) {
        super(name, price, cursed, quality);
        this.id = id;
        this.basePrice = basePrice;
        this.materialGroup = materialGroup;
        this.mass = mass;
    }

    public String getId() {
        return id;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getMaterialGroup() {
        return materialGroup;
    }

    // operations on mass
    public double getMass() {
        return mass;
    }
    private void addMass (double mass) {
        this.mass += mass;
    }
    protected boolean removeMass (double mass) {
        if (mass <= this.mass) {
            this.mass -= mass;
            return true;
        } else {
            return false;
        }
    }

    // add more material to the lot and calculate its new quality value
    protected void addMoreMaterial (double addedMaterialMass, MagicalObject.Quality addedMaterialQuality) {
        //calculate new quality value:
        double newQualityValue = (this.mass * this.quality.getValue() + addedMaterialMass * addedMaterialQuality.getValue())/(this.mass + addedMaterialMass);
        int newQualityValueRounded = (int) Math.ceil(newQualityValue);
        this.quality = Quality.getQualityByValue(newQualityValueRounded);
        addMass(addedMaterialMass);
    }
}
