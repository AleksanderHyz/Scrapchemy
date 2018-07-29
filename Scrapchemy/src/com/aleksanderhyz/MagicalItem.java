package com.aleksanderhyz;

/**
 * Items that Player can buy in the Market or resell them
 * They're built from Magical Item Components, defined by type of Magical Item
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MagicalItem extends MagicalObject {

    private List<MagicalItemComponent> components;


    @Override
    void calculatePrice() {
        double componentPrices = 0;
        double componentQualities = 0;
        for (MagicalItemComponent component : components) {
            componentPrices += component.getPrice();
            componentQualities += component.getQuality().getValue();
        }
        this.price = componentPrices * (componentQualities/5);
    }

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
            return Player.TransactionStatus.ITEM_NOT_AVAILABLE;
        }
    }

    @Override
    public Player.TransactionStatus sell(Player player) {
        return null;
    }


    public List<MagicalItemComponent> getComponents() {
        return components;
    }

    protected MagicalItem() {
        super(null,0,false,null);
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

        this.name = fields.get(0);

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
                String componentName = (String) componentFields.get(1);
                double componentBasePrice = (double) componentFields.get(4);
                boolean componentCurseStatus = this.cursed;
                Quality componentQuality = Quality.getRandomQuality();
                double componentMass = (double) componentFields.get(3);
                List<String> componentPossibleMaterials = new ArrayList<>(databaseConnection.getMaterialsFromGroup((String)componentFields.get(2)));
                int randomMaterialListIndex = randomIndex(componentPossibleMaterials.size());
                String componentMaterial = componentPossibleMaterials.get(randomMaterialListIndex);
                MagicalItemComponent magicalItemComponent = new MagicalItemComponent(componentName, 0, componentCurseStatus, componentQuality, componentBasePrice,componentMass, componentMaterial);
                // name, price, cursed, quality, basePrice, mass, material id
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


        // calculate item price
        double itemPrice = 0;
        int itemComponentsQualityValueSum = 0;
        for (int i = 0; i < this.components.size(); i++) {
            itemPrice += this.components.get(i).getBasePrice();
            itemComponentsQualityValueSum += this.components.get(i).getQuality().getValue();
        }
        itemPrice = itemPrice * (itemComponentsQualityValueSum/5);
        this.price = itemPrice;



        databaseConnection.close();
    }

}
