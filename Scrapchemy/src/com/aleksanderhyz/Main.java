package com.aleksanderhyz;

public class Main {

    private static boolean quitGame = false;

    public static void main(String[] args) {

        // start game
        System.out.println("Welcome to Scrapchemy.\n" +
                "Type in your command:\n" +
                "Start new game: " + KeyboardInput.GameChoice.START_NEW_GAME.getCommand() + "\n" +
                "Load saved game: " + KeyboardInput.GameChoice.LOAD_GAME.getCommand() + "\n" +
                "Quit game: " + KeyboardInput.GameChoice.QUIT.getCommand());
        while (true) {
            KeyboardInput.GameChoice command = KeyboardInput.gameChoice();
            if (command.equals(KeyboardInput.GameChoice.QUIT)) {
                quitGame = true;
                break;
                // game quit, this loop ends, the next one does not start because quitGame is true, program ends
            } else if (command.equals(KeyboardInput.GameChoice.START_NEW_GAME)) {
                System.out.print("Starting new game.\n" +
                        "Enter new player's name: ");
                String newName = KeyboardInput.enterPlayerName();
                Player player = new Player(newName);
                break;
                // new game starts
            } else if (command.equals(KeyboardInput.GameChoice.LOAD_GAME)) {
                // loading saved game from .db file
                /*TO BE MADE*/
            } else {
                System.out.println("Wrong command.");
            }
        }

        // main game
        while (!quitGame) {

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

// game start
    private static void gameStart () {
        KeyboardInput.GameChoice command = KeyboardInput.gameChoice();
        switch (command) {
            case START_NEW_GAME:
                // start new game, create new player
                StartGame();
                break;
            case LOAD_GAME:
                // load previously saved game from a save .db file
                LoadGame();
                break;
            case QUIT:
                // quitGame game, end the program
                quitGame = true;
                break;
            default:
                gameStart();
                break;
        }
    }

}