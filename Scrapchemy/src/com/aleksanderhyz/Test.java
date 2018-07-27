package com.aleksanderhyz;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        DatabaseConnection databaseConnection = new DatabaseConnection();

        if (!databaseConnection.open()) {
            System.out.println("Can't open database");
        } else {
            System.out.println("Connected successfully.");
        }


        List<Integer> testItems = new ArrayList<>(databaseConnection.getMaterialsFromGroup("1"));

        for (int i = 0; i < testItems.size(); i++) {
            System.out.println(testItems.get(i));
        }


        System.out.println("=============================");
        System.out.println(databaseConnection.count(DatabaseConnection.MAGICAL_PRODUCT_TABLE));


        databaseConnection.close();
    }
}
