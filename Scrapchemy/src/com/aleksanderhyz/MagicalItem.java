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

    protected MagicalItem(double price, boolean cursed, Quality quality) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.open();

        // choose random item from the list
            // the item is chosen from the whole table, by _id, from 1 to max
        int magicalItemCount = databaseConnection.count(DatabaseConnection.MAGICAL_ITEM_TABLE);
        int max = randomIndex(magicalItemCount) + 1;
        String itemID = Integer.toString(max);
        List<String> fields = new ArrayList<>(databaseConnection.getMagicalItemByID(itemID));
        // 1 - name, 2 - component1, 3 - component2, 4 - component3

        this.name = fields.get(1);

        // decide randomly if it's cursed or not
        Random curse = new Random();
        this.cursed = curse.nextBoolean();

        // generate components (as cursed or not, randomly choose quality of each)
            // number of components may vary, with max being 3, if there's less then the remaining ones' _id's are NULL
        for (int i = 2; i < fields.size(); i++) {
            if (fields.get(i) != null) {
                List<Object> componentFields = new ArrayList<>(databaseConnection.getMagicalItemComponentsByID(fields.get(i)));
                // 1 - _id, 2 - name, 3 - material group, 4 - mass, 5 - base price
                String componentName = (String) componentFields.get(2);
                double componentBasePrice = (double) componentFields.get(5);
                boolean componentCurseStatus = this.cursed;
                Quality componentQuality = Quality.getRandomQuality();
                double componentMass = (double) componentFields.get(4);
                List<String> componentPossibleMaterials = new ArrayList<>(databaseConnection.getMaterialsFromGroup((String)componentFields.get(3)));
                MagicalItemComponent magicalItemComponent = new MagicalItemComponent(componentName, 0, componentCurseStatus, componentQuality, componentBasePrice,componentMass,);
                // name, price, cursed, quality, basePrice, mass, material id
                magicalItemComponent.calculatePrice();
            }
        }


        databaseConnection.close();

        // calculate item price


    }


//    protected MagicalItem(String name, double price, boolean cursed, Quality quality) {
//        super(name, price, cursed, quality);
//    }
}
