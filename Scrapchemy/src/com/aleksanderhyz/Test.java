package com.aleksanderhyz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {

    public static void main(String[] args) {


        DatabaseConnection databaseConnection = new DatabaseConnection();

        if (!databaseConnection.open()) {
            System.out.println("Can't open database");
        } else {
            System.out.println("Connected successfully.");
        }


        List<String> testItems = new ArrayList<>(databaseConnection.getMaterialsFromGroup("1"));

        for (int i = 0; i < testItems.size(); i++) {
            System.out.println(testItems.get(i));
        }


        System.out.println("=============================");
        System.out.println(databaseConnection.count(DatabaseConnection.MAGICAL_PRODUCT_TABLE));

        System.out.println("=============================");

        List<String> testItemFields = new ArrayList<>(databaseConnection.getMagicalItemByID("6"));

        for (int i = 0; i < testItemFields.size(); i++) {
            System.out.println(testItemFields.get(i));
        }


        System.out.println("=============================");

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int testSum = (random.nextInt(10) + 1) + (random.nextInt(10) + 1) + (random.nextInt(10) + 1);
            int testAverage = (int) Math.ceil(testSum / 3);
            System.out.println(testAverage);
        }

        int testSum = 10 + 10 + 10;
        int testAverage = (int) Math.ceil(testSum / 3);
        System.out.println(testAverage);

        databaseConnection.close();

    }
}
