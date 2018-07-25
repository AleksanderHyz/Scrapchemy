package com.aleksanderhyz;

public class MagicalProduct extends MagicalObject {

    @Override
    void calculatePrice() {

    }

    @Override
    public Player.TransactionStatus sell(Player player) {
        return null;
    }

    protected MagicalProduct(String name, double price, boolean cursed, Quality quality) {
        super(name, price, cursed, quality);
    }
}
