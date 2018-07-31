package com.aleksanderhyz;

public class MagicalMaterial extends MagicalObject {

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

    @Override
    public Player.TransactionStatus sell(Player player) {
        return null;
    }

    // creating Magical Material from a Magical Item Component
    public MagicalMaterial(String name, double price, boolean cursed, Quality quality, String id, double basePrice, String materialGroup, double mass) {
        super(name, price, cursed, quality);
        this.id = id;
        this.basePrice = basePrice;
        this.materialGroup = materialGroup;
        this.mass = mass;
    }
}
