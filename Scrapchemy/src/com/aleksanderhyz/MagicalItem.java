package com.aleksanderhyz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Items that Player can buy in the Market or resell them
 * They're built from Magical Item Components, defined by type of Magical Item
 */

public class MagicalItem extends MagicalObject {

    // when selling Magical Item the Player will get 0.6 of it's price in money

    private List<MagicalItemComponent> components;


    // Player buying a Magical Item from the Market
    public Player.TransactionStatus buy (Player player) {
        if (player.getMarket().contains(this)) {
            if (player.updateWallet(this.price, Player.TransactionType.BUYING))
                /* updateWallet will return false in this case if the Player has not enough money in the wallet */ {
                player.getMagicalItems().add(this);
                player.getMarket().remove(this);
                return Player.TransactionStatus.BOUGHT_SUCCESSFULLY;
            } else {
                return Player.TransactionStatus.NOT_ENOUGH_MONEY;
            }
        } else {
            return Player.TransactionStatus.OBJECT_NOT_AVAILABLE;
            // option included in case of program somehow chooses item from a different list
            // for example, if item would be chosen by name or ID
            // or in case of multithreading, when program deletes item from the Market while Player was trying to perform transaction
        }
    }

    // Player selling the item they have in their inventory
    @Override
    public Player.TransactionStatus sell(Player player) {
        if (player.getMagicalItems().contains(this)) {
            if(player.updateWallet((this.price * SELLING_MODIFIER), Player.TransactionType.SELLING)) {
                player.getMagicalItems().remove(this);
                return Player.TransactionStatus.SOLD_SUCCESSFULLY;
            }
            return null;
        } else {
            return Player.TransactionStatus.OBJECT_NOT_AVAILABLE;
        }
    }


    public List<MagicalItemComponent> getComponents() {
        return components;
    }

    // Magical Items can only be generated randomly, using data read from database
    protected MagicalItem() {
        super(null,null,0,false,null);
        // fields from MagicalObject: name, price, cursed, quality
        // fields from here: components
        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.open();

        // choose random item from the list
            // the item is chosen from the whole table, by _id, from 1 to max
        int magicalItemCount = databaseConnection.count(DatabaseConnection.MAGICAL_ITEM_TABLE);
        int max = randomIndex(magicalItemCount) + 1;
        String itemID = Integer.toString(max);
        List<String> fields = new ArrayList<>(databaseConnection.getMagicalItemByID(itemID));
        // 0 - name, 1 - component1, 2 - component2, 3 - component3


        // decide randomly if it's cursed or not
        Random curse = new Random();
        this.cursed = curse.nextBoolean();

        this.components = new ArrayList<>();

        // generate components (as cursed or not, randomly choose quality of each)
            // number of components may vary, with max being 3, if there's less then the remaining ones' _id's are NULL
        for (int i = 1; i < fields.size(); i++) {
            if (fields.get(i) != null) {
                List<Object> componentFields = new ArrayList<>(databaseConnection.getMagicalItemComponentsByID(fields.get(i)));
                // 0 - _id, 1 - name, 2 - material group, 3 - mass, 4 - base price
                String componentId = (String) componentFields.get(0);
                String componentName = (String) componentFields.get(1);
                double componentBasePrice = (double) componentFields.get(4);
                boolean componentCurseStatus = this.cursed;
                Quality componentQuality = Quality.getRandomQuality();
                double componentMass = (double) componentFields.get(3);
                List<String> componentPossibleMaterials = new ArrayList<>(databaseConnection.getMaterialsFromGroup((String)componentFields.get(2)));
                int randomMaterialListIndex = randomIndex(componentPossibleMaterials.size());
                String componentMaterial = componentPossibleMaterials.get(randomMaterialListIndex);
                MagicalItemComponent magicalItemComponent = new MagicalItemComponent(componentId, componentName, 0, componentCurseStatus, componentQuality, componentBasePrice,componentMass, componentMaterial);
                //id, name, price, cursed, quality, basePrice, mass, material id
                magicalItemComponent.calculatePrice();
                this.components.add(magicalItemComponent);
            }
        }

        // calculate the quality of whole Magical Item:
            // quality value of whole item is average value of components
        int itemQualityValue = 0;
        for (int i = 0; i < this.components.size(); i++) {
            itemQualityValue += this.components.get(i).getQuality().getValue();
        }
        itemQualityValue = (int) Math.ceil(itemQualityValue / this.components.size());
        this.quality = Quality.getQualityByValue(itemQualityValue);

        // build name:
         // [QUALITY] quality [CURSED/null] [item name]
        StringBuilder fullName = new StringBuilder(this.quality.getVisibleQuality() + " quality ");
        if (this.cursed) {
            fullName.append("CURSED ");
        }
        fullName.append(fields.get(0));
        this.name = fullName.toString();

        this.id = itemID;

        // calculate item price
        calculatePrice();

        databaseConnection.close();
    }

    @Override
    void calculatePrice() {
        double componentPrices = 0;
        double componentQualities = 0;
        for (MagicalItemComponent component : this.components) {
            componentPrices += component.getBasePrice();
            componentQualities += component.getQuality().getValue();
        }
        this.price = componentPrices * (componentQualities/5);
    }

    protected List<MagicalItemComponent> dismantleMagicalItem () {
        List<MagicalItemComponent> salvagedComponents = new ArrayList<>();
        for (MagicalItemComponent component : this.components) {
//            if (component.getQuality().getValue() > 0) {
//                // 0 means Quality is UNACCEPTABLE so the component isn't salvaged (gets automatically scrapped)
//                salvagedComponents.add(component.salvageMagicalItemComponent());
//            }
            // change: the UNACCEPTABLE quality components will be added to the returned list
            // and only scrapped when salvaged components are added to Player's inventory
            // so between these actions information about these components is displayed for the Player
            salvagedComponents.add(component.salvageMagicalItemComponent());
        }
        return salvagedComponents;
    }

    @Override
    public String toString() {
        return this.name;
        // name is fullName, created in the constructor in this scheme:
            // [QUALITY] quality [CURSED/null] [item name]
    }
}
