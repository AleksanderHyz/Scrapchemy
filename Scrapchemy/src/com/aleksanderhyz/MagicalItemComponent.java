package com.aleksanderhyz;

/**
 *  Components are parts the Magical Items are made of
 */

public class MagicalItemComponent extends MagicalObject {

    private double basePrice;
    private double mass;

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

    @Override
    public Player.TransactionStatus sell(Player player) {
        return null;
    }
}
