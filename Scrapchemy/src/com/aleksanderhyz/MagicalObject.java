package com.aleksanderhyz;

import java.util.Random;

/**
 *  Abstract class to define fields and methods for all kinds of magical objects the player can buy, obtain or sell.
 *  Classes extending this one:
 *  MagicalItem
 *  MagicalItemComponent
 *  MagicalMaterial
 *  MagicalProduct
 */

public abstract class MagicalObject {

    public static final String UNACCEPTABLE_QUALITY = "UNACCEPTABLE";
    public static final String LOW_QUALITY = "LOW";
    public static final String MODERATE_QUALITY = "MODERATE";
    public static final String HIGH_QUALITY = "HIGH";
    public static final String EXCELLENT_QUALITY = "EXCELLENT";

    public static final double SELLING_MODIFIER = 0.6;

    protected String id;
    protected String name;
    protected double price;
    protected boolean cursed;
    protected Quality quality;

    protected enum Quality {
        // Quality of a Magical Object influences the price its sold for.
        // What influences how much an object (and objects containing it) is worth is the numerical value, which player can't see during the game.
        // The player can only the Visible Quality, which only narrows down the actual value, thus they won't know the exact price of Magical Product they made.

        QUALITY_01(1),      // UNACCEPTABLE - Player can't sell or use Magical Objects of this quality, it's automatically discarded
        QUALITY_02(2),      // LOW
        QUALITY_03(3),      // LOW
        QUALITY_04(4),      // LOW
        QUALITY_05(5),      // MODERATE
        QUALITY_06(6),      // MODERATE
        QUALITY_07(7),      // HIGH
        QUALITY_08(8),      // HIGH
        QUALITY_09(9),      // HIGH
        QUALITY_10(10);     // EXCELLENT

        private int value;
        private String visibleQuality;

        Quality (int value) {
            this.value = value;
            this.visibleQuality = setVisibleQualityValue();
        }

        public static Quality getRandomQuality () {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }

        public static Quality getQualityByValue (int value) {
            // value given for the method is calculated earlier as average of 1-10 values using math.ceil() so it's in range 1-10
            // or retrieved from other object as integer in 1-10 range
            // so as proper index number it needs to be decreased by 1
            try {
                return values()[value - 1];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Couldn't get the quality: " + e.getMessage());
                return null;
            }
        }

        public int getValue() {
            return value;
        }

        private String setVisibleQualityValue() {
            if (this.value == 1) {
                return UNACCEPTABLE_QUALITY;
            } else if (this.value >= 2 && this.value <= 4) {
                return LOW_QUALITY;
            } else if (this.value >= 5 && this.value <= 6) {
                return MODERATE_QUALITY;
            } else if (this.value >= 7 && this.value <= 9) {
                return HIGH_QUALITY;
            } else if (this.value == 10) {
                return EXCELLENT_QUALITY;
            }
            return null;
        }

        public String getVisibleQuality() {
            return visibleQuality;
        }
    }

    protected MagicalObject(String id, String name, double price, boolean cursed, Quality quality) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cursed = cursed;
        this.quality = quality;
    }

    public Quality getQuality() {
        return quality;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isCursed() {
        return cursed;
    }

    abstract void calculatePrice();

    // "buy" operation is in the MagicalItem class, because that's the only kind of MagicalObject the Player can buy

    abstract public Player.TransactionStatus sell (Player player);


    // choosing random Magical Object index from a specified range
    // this is to get index number of _id on the ArrayList it was put on previously, ranging from 0 to maximum
    // it does NOT return the actual _id
    public int randomIndex (int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

}
