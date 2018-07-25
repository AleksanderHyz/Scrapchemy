package com.aleksanderhyz;

public class Test {

    public static void main(String[] args) {

        DatabaseConnection databaseConnection = new DatabaseConnection();

        if (!databaseConnection.open()) {
            System.out.println("Can't open database");
        } else {
            System.out.println("Connected successfully.");
        }




        databaseConnection.close();
    }
}
