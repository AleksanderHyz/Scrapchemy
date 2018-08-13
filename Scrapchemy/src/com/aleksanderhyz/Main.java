package com.aleksanderhyz;

import java.util.List;

public class Main {

    // instance of Player, this object is a singleton
    // fields are defined when user chooses to start new game or load a saved file
    private static Player player;

    // variable which decides when to quit game
    private static boolean quitGame = false;

    // instance of GameChoice responsible for recognizing commands written by the Player
    private static KeyboardInput.GameChoice command;


    public static void main(String[] args) {

        // start game
        System.out.println("Welcome to Scrapchemy.\n" +
                "Type in your command:\n" +
                "Start new game: " + KeyboardInput.GameChoice.START_NEW_GAME.getCommand() + "\n" +
                "Load saved game: " + KeyboardInput.GameChoice.LOAD_GAME.getCommand() + "\n" +
                "Quit game: " + KeyboardInput.GameChoice.QUIT.getCommand());
        while (true) {
            command = KeyboardInput.gameChoice();
            if (command.equals(KeyboardInput.GameChoice.QUIT)) {
                quitGame = true;
                break;
                // game quit, this loop ends, the next one does not start because quitGame is true, program ends
            } else if (command.equals(KeyboardInput.GameChoice.START_NEW_GAME)) {
                System.out.print("Starting new game.\n" +
                        "Enter new player's name: ");
                // creating new Player with the given name
                String newName = KeyboardInput.enterPlayerName();
                player = new Player(newName);
                // bringing items to buy at the Market
                player.refreshMarket();
                // adding commissions
                for (int i = 1; i <= 5; i++){
                    player.addNewCommission();
                }
                printSeparator();
                playerStatusDisplay();
                break;
                // new game starts
            } else if (command.equals(KeyboardInput.GameChoice.LOAD_GAME)) {
                // loading saved game from .db file
                /*TO BE MADE*/
            } else {
                System.out.println("Wrong command.");
            }
        }

        // main game loop
        while (!quitGame) {
            printSeparator();
            System.out.println("Choose action:\n" +
                    "Go to market: " + KeyboardInput.GameChoice.GO_TO_MARKET.getCommand() + "\n" +
                    "Look at your inventory: " + KeyboardInput.GameChoice.GO_TO_INVENTORY.getCommand() + "\n" +
                    "Show commissions you got: " + KeyboardInput.GameChoice.GO_TO_COMMISSIONS.getCommand() + "\n" +
                    "Pause game: " + KeyboardInput.GameChoice.PAUSE_GAME.getCommand());
            command = KeyboardInput.gameChoice();
            printSeparator();
            if (command.equals(KeyboardInput.GameChoice.GO_TO_MARKET)) {
                // going to Market to buy Magical Items or sell Magical Objects
                System.out.println("Currently available at the market:");
                player.printMarket();
                System.out.println("\n Choose your action: " + KeyboardInput.GameChoice.BUY.getCommand() + ", " + KeyboardInput.GameChoice.SELL.getCommand() + "\n");
                command = KeyboardInput.gameChoice();
                if (command.equals(KeyboardInput.GameChoice.BUY)) {
                    // buying new item
                    buyItemFromMarket();
                } else if (command.equals(KeyboardInput.GameChoice.SELL)) {
                    // selling an object from inventory
                }

            } else if (command.equals(KeyboardInput.GameChoice.GO_TO_INVENTORY)) {
                // going to inventory to see what's there and process Magical Objects into different ones
            } else if (command.equals(KeyboardInput.GameChoice.GO_TO_COMMISSIONS)) {
                // looking at commissions to check needed ingredients and fulfill them
            } else if (command.equals(KeyboardInput.GameChoice.PAUSE_GAME)) {
                // pausing the game to save or quit
            } else {
                System.out.println("Wrong command.");
            }
        }
    }


//fulfill commission method:
//// method that reads which materials the Player chooses, using Scanner
//// then they're checked for compatibility with the commission's recipe
//    // move all materials from the inventory to a temporary list
//    // in order to delete all chosen materials from it during the process of choosing ingredients
//    // but without deleting them from inventory in case product in the end is not created
//    //List<MagicalMaterial> temporaryMaterialList = this.magicalMaterials;
//

    // print separator:
    private static void printSeparator () {
        System.out.println("==========================================================================");
    }

    // Unicode for used symbols:
        // \u20AC - Euro currency symbol

    // standard Player status display
    private static void playerStatusDisplay () {
        System.out.println(player.getName() + "'s current status:\n" +
                "Money: " + player.getWallet() + " \u20AC\n" +
                "Commissions completed: " + player.getCommissionsCompleted() + "\n\n" +
                "Items in inventory: " + player.getMagicalItems().size() + "\n" +
                "Components in inventory: " + player.getMagicalComponents().size() + "\n" +
                "Materials in inventory: " + player.getMagicalMaterials().size() + "\n\n" +
                "Items available at the market: " + player.getMarket().size() + "\n" +
                "Commissions available: " + player.getCommissionList().size());

    }

    // buying Magical Item from the market
    private static void buyItemFromMarket () {
        System.out.print("Choose item from the market (type in the number): ");
        MagicalItem chosenItem = (MagicalItem) getObjectByIndex(player.getMarket());

    }


    // choosing item from list by index number given from keyboard input
    private static Object getObjectByIndex (List list) {
        Object object = null;
        boolean indexInRange = false;
        while (!indexInRange) {
            int index = KeyboardInput.getIndexNumber();
            try {
                object = list.get(index);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Number out of range: " + e.getMessage() +
                "\nTry again: ");
            }
            indexInRange = (object != null) ? true : false;
        }
        return object;
    }
}