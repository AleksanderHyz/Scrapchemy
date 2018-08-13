package com.aleksanderhyz;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *  Singleton class that defines the game progress
 */

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
        OBJECT_NOT_AVAILABLE,
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

    public void addNewItemToMarket () {
        MagicalItem magicalItem = new MagicalItem();
        // new random Magical Item is created
        this.market.add(magicalItem);
    }

    public void refreshMarket () {
        // replace old market (deleting remaining items)
        // with new list with 3 fresh items
        List<MagicalItem> newMarket = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            MagicalItem magicalItem = new MagicalItem();
            newMarket.add(magicalItem);
        }
        this.market = newMarket;
    }

    public void addNewCommission () {
        MagicalProduct newCommission = new MagicalProduct();
        this.commissionList.add(newCommission);
    }

    // showing list of items for sale, with numbers and prices
    public void printMarket () {
        for (int i = 0; i < market.size(); i++ ) {
            System.out.println(i+1 + ". " + market.get(i).toString() + ", " + market.get(i).getPrice() + " \u20AC" /*Euro currency symbol*/);
            // .toString is overridden in MagicalObject subclasses to show proper data
        }
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

    // showing list of items with numbers
    public void printInventory (List list /* magicalItems, magicalComponents, magicalMaterials or commissionList */) {
        for (int i = 0; i < list.size(); i++ ) {
            System.out.println(i+1 + ". " + list.get(i).toString());
            // .toString is overridden in MagicalObject subclasses to show proper data
        }
    }

    public boolean dismantleMagicalItem(MagicalItem magicalItem) {
        if (this.magicalItems.contains(magicalItem)) {
            for (MagicalItemComponent component : magicalItem.dismantleMagicalItem()) {
                if (component.getQuality().getValue() > 0) {
                    // 0 means Quality is UNACCEPTABLE so the component isn't salvaged (gets automatically scrapped)
                    this.magicalComponents.add(component);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // processing Component from inventory to get new material
    public boolean processMagicalItemComponent (MagicalItemComponent magicalItemComponent) {
        if (this.magicalComponents.contains(magicalItemComponent)) {
            MagicalMaterial salvagedMagicalMaterial = magicalItemComponent.processMagicalItemComponent();
            this.magicalComponents.remove(magicalItemComponent);

            // checking if same material of that quality and curse status and is already on the list
                // if so then just new material's mass is added to it
            List<MagicalMaterial> searchedMaterial = new ArrayList<>(filterMaterialByID(this.magicalMaterials, salvagedMagicalMaterial.getId()));
            if (!searchedMaterial.isEmpty()) {
                searchedMaterial = filterMaterialsByCurse(searchedMaterial, salvagedMagicalMaterial.cursed);
                if (!searchedMaterial.isEmpty()) {
                    searchedMaterial = filterMaterialsByQuality(searchedMaterial, salvagedMagicalMaterial.getQuality().getVisibleQuality());
                    if (!searchedMaterial.isEmpty()) {
                        // there should be only one material left (with index 0)
                        searchedMaterial.get(0).addMoreMaterial(salvagedMagicalMaterial.getMass(), salvagedMagicalMaterial.getQuality());
                        // added salvaged material to the previously existing one of the same visible quality and curse status
                    }
                }
            } else {
                this.magicalMaterials.add(salvagedMagicalMaterial);
            }

            return true;
        } else {
            return false;
        }
    }

    //filtering materials from inventory
        // get materials by ID
    protected List<MagicalMaterial> filterMaterialByID(List<MagicalMaterial> magicalMaterialsList, String ID) {
        List<MagicalMaterial> filteredMaterials = new ArrayList<>();
        for (MagicalMaterial magicalMaterial : magicalMaterialsList) {
            if (magicalMaterial.getId().equals(ID)) {
                filteredMaterials.add(magicalMaterial);
            }
        }
        return filteredMaterials;
    }
        // get materials by curse status
    protected List<MagicalMaterial> filterMaterialsByCurse (List<MagicalMaterial> magicalMaterialsList, boolean requiredCurseStatus) {
        List<MagicalMaterial> filteredMaterials = new ArrayList<>();
        for (MagicalMaterial magicalMaterial : magicalMaterialsList) {
            if (magicalMaterial.isCursed() == requiredCurseStatus) {
                filteredMaterials.add(magicalMaterial);
            }
        }
        return filteredMaterials;
    }
        // get materials by visible quality
    protected List<MagicalMaterial> filterMaterialsByQuality (List<MagicalMaterial> magicalMaterialsList, String requiredVisibleQuality) {
        List<MagicalMaterial> filteredMaterials = new ArrayList<>();
        for (MagicalMaterial magicalMaterial : magicalMaterialsList) {
            if (magicalMaterial.getQuality().getVisibleQuality().equals(requiredVisibleQuality)) {
                filteredMaterials.add(magicalMaterial);
            }
        }
        return filteredMaterials;
    }



    // fulfilling commissions:

    public enum CommissionStatus {
        COMPLETING_POSSIBLE,
        LACK_OF_MATERIALS,
        LACK_OF_CURSED_MATERIALS,
        LACK_OF_NON_CURSED_MATERIALS,
        COMPLETED_SUCCESSFULLY,
        PRODUCT_NOT_ON_COMMISSION_LIST,
        ALL_MATERIALS_CURSED
    }

    public enum MaterialCheck {
        RIGHT_MATERIAL,
        WRONG_MATERIAL,
        MATERIAL_NOT_FOUND,
        NOT_ENOUGH_MATERIAL,
        MATERIAL_CURSED,
        LACK_OF_CURSED_MATERIAL,
        NOT_FUEL,
        RIGHT_FUEL
    }

    public List<MagicalProduct> getCommissionList() {
        return commissionList;
    }
    public int getCommissionsCompleted() {
        return commissionsCompleted;
    }

    // method to check if Player has enough materials in their inventory needed to fulfill a certain commission
//    public CommissionStatus queryInventoryForCommissionIngredients (MagicalProduct commission) {
//        if (this.commissionList.contains(commission)) {
//            List<MagicalMaterial> filteredMaterials = new ArrayList<>();
//
//            // filter materials by the curse status if it's CLEAN
//            if (commission.getRequiredCursedStatus().equals(MagicalProduct.RequiredCursedStatus.CLEAN)) {
//                filteredMaterials = filterMaterialsByCurse(this.magicalMaterials, false);
//                if (filteredMaterials.isEmpty()) {
//                    return CommissionStatus.ALL_MATERIALS_CURSED;
//                }
//            }
//
//
//
//        } else {
//            return CommissionStatus.PRODUCT_NOT_ON_COMMISSION_LIST;
//        }
//    }
        // change of plans, materials will be checked only after Player chooses them
        // so determining whether their supplies are enough is on their side, being part of the challenge

//    public CommissionStatus fulfillCommission (MagicalProduct commission, List<MagicalMaterial> chosenIngredients, MagicalMaterial chosenFuel) {
//        if (this.commissionList.contains(commission)) {
//            if (commission.getRequiredCursedStatus().equals(MagicalProduct.RequiredCursedStatus.CURSED)) {
//                // checking if any of the used ingredients or fuel is cursed
//                // at least one has to be in order to make the whole product cursed
//
//                boolean productCursed = false;
//
//                // check all ingredients
//                for (MagicalMaterial ingredient : chosenIngredients) {
//                    if (ingredient.isCursed()) {
//                        productCursed = true;
//                    }
//                }
//                // check fuel
//                if (chosenFuel.isCursed()) {
//                    productCursed = true;
//                }
//
//                if (!productCursed) {
//                    return CommissionStatus.LACK_OF_CURSED_MATERIALS;
//                } // if else just go further
//
//            }
//
//        } else {
//            return CommissionStatus.PRODUCT_NOT_ON_COMMISSION_LIST;
//        }
//    }
    // change of plans, there will be a method in main that calls for each ingredient and requires the player to choose material(s) matching ingredient currently asked for
    // in that way there won't be a problem with different order of ingredients required and given ones
    // also it will be easier to use materials from different stocks (of different quality and curse status) as one ingredient

    public MaterialCheck materialCheck (MagicalProduct.MagicalProductIngredient magicalProductIngredient, MagicalMaterial chosenMagicalMaterial, MagicalProduct.RequiredCursedStatus requiredCursedStatus) {

        // first check id
        if (chosenMagicalMaterial.getId().equals(magicalProductIngredient.getMaterialID())) {

            // if the product's required status is CLEAN then no used material can be cursed
            if (requiredCursedStatus.equals(MagicalProduct.RequiredCursedStatus.CLEAN)) {
                if (chosenMagicalMaterial.isCursed()) {
                    return MaterialCheck.MATERIAL_CURSED;
                } // if else just go on
            } // if else just go on

            // checking mass
            if (chosenMagicalMaterial.getMass() < magicalProductIngredient.getMass()) {
                return MaterialCheck.NOT_ENOUGH_MATERIAL;
            }

            // all requirements met
            return MaterialCheck.RIGHT_MATERIAL;

        } else {
            return MaterialCheck.WRONG_MATERIAL;
        }
    }

    public MaterialCheck fuelCheck (MagicalProduct magicalProduct, MagicalMaterial chosenFuel) {

        // only one object is given as fuel, but there is an option to combine various materials
        // as fuel for one product, then they're combined into one material, with mass of all of them
        // and cursed if any of the used fuel ingredients was cursed
        // all that is covered in different method [METHOD TO BE WRITTEN]

        // check if given material is suitable for fuel (is in wood group)
        if (chosenFuel.getMaterialGroup().equals(MagicalMaterial.WOOD_GROUP_ID)) {

            // if the product's required status is CLEAN then fuel can't be cursed
            if (magicalProduct.getRequiredCursedStatus().equals(MagicalProduct.RequiredCursedStatus.CLEAN)) {
                if (chosenFuel.isCursed()) {
                    return MaterialCheck.MATERIAL_CURSED;
                }
            }

            // checking mass
            if (chosenFuel.getMass() < magicalProduct.getFuelMass()) {
                return MaterialCheck.NOT_ENOUGH_MATERIAL;
            }

            // all requirements met
            return MaterialCheck.RIGHT_FUEL;

        } else {
            return MaterialCheck.NOT_FUEL;
        }
    }

}
