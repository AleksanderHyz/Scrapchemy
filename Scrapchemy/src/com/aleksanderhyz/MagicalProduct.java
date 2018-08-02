package com.aleksanderhyz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MagicalProduct extends MagicalObject {

    private RequiredCursedStatus requiredCursedStatus;
    private double fuelMass;
    private List<MagicalProductIngredient> ingredients;
    private int commissionNumber;

    protected enum RequiredCursedStatus {
        CURSED,     // 0
        CLEAN,      // 1
        NO_MATTER;  // 2, 3, 4, 5

        private static RequiredCursedStatus rollStatus () {
            Random random = new Random();
            int roll = random.nextInt(6);
            if (roll == 0) {
                return CURSED;
            } else if (roll == 1) {
                return CLEAN;
            } else {
                return NO_MATTER;
            }
        }


        @Override
        public String toString() {
            String name;
            if (this.equals(CURSED)) {
                name = "CURSED ";
            } else if (this.equals(CLEAN)) {
                name = "CLEAN ";
            } else {
                name = null;
            }
            return name;
        }
    }


    @Override
    void calculatePrice() {

    }

    @Override
    public Player.TransactionStatus sell(Player player) {
        return null;
    } // Magical Product is not to be sold, there's a different method for monetizing it

    // generate Magical Product as a new commission:
    protected MagicalProduct() {
        super(null, 0, false, null);
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.open();

        // choose random product from the database
        int magicalProductCount = databaseConnection.count(DatabaseConnection.MAGICAL_PRODUCT_TABLE);
        int max = randomIndex(magicalProductCount) + 1;
        String productID = Integer.toString(max);
        List<Object> fields = new ArrayList<>(databaseConnection.getMagicalProductByID(productID));
        // 0 - id, 1 - name, 2 - fuel mass,
        // 3 - material1 _id, 4 - material1 mass, 5 - material2 _id, 6 - material2 mass, 7 - material3 _id, 8 - material3 mass
        String productName = (String) fields.get(1);
        double productFuelMass = (double) fields.get(2);
        //getting ingredients from fields numbered 3-8 on the list, the number of them may vary
        List<MagicalProductIngredient> magicalProductIngredients = new ArrayList<>();
        for (int i = 3; i < 8; i += 2) {
            if (fields.get(i) != null) {
                String ingredientID = (String) fields.get(i);
                double ingredientMass = (Double) fields.get(i+1);
                MagicalProductIngredient magicalProductIngredient = new MagicalProductIngredient(ingredientID, ingredientMass);
                magicalProductIngredients.add(magicalProductIngredient);
            }
        }

        this.fuelMass = productFuelMass;
        this.ingredients = magicalProductIngredients;
        this.requiredCursedStatus = RequiredCursedStatus.rollStatus();
        this.commissionNumber = hashCode();

        //generate full name:
            // [CURSED/CLEAN/null] [name]
        StringBuilder commissionFullName = new StringBuilder(this.requiredCursedStatus.toString());
        commissionFullName.append(productName);
        this.name = commissionFullName.toString();

        databaseConnection.close();
    }

    private class MagicalProductIngredient {
        private String materialID;
        private double mass;

        public MagicalProductIngredient(String materialID, double mass) {
            this.materialID = materialID;
            this.mass = mass;
        }
    }
}
