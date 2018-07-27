package com.aleksanderhyz;

/**
 * Items that Player can buy in the Market or resell them
 * They're built from Magical Item Components, defined by type of Magical Item
 */

import java.util.List;

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

    protected MagicalItem(String name, double price, boolean cursed, Quality quality) {
        // choose random item from the list
        // decide randomly if it's cursed or not
        // generate components (as cursed or not, randomly choose quality of each)
        // calculate item price
        super(name, price, cursed, quality);
    }
}
