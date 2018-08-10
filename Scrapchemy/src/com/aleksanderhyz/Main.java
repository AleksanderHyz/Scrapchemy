package com.aleksanderhyz;

public class Main {

    private static boolean quitGame = false;

    public static void main(String[] args) {

        while (!quitGame) {
            // start game
            System.out.println("Welcome to Scrapchemy.\n" +
                    "Type in your command:\n" +
                    "Start new game: " + KeyboardInput.GameChoice.START_NEW_GAME.getCommand() + "\n" +
                    "Load saved game: " + KeyboardInput.GameChoice.LOAD_GAME.getCommand() + "\n" +
                    "Quit game: " + KeyboardInput.GameChoice.QUIT.getCommand());
            KeyboardInput.GameChoice command = KeyboardInput.gameChoice();
            if (command.equals(KeyboardInput.GameChoice.QUIT)) {
                quitGame = true;
                return;
            } else if (command.equals(KeyboardInput.GameChoice.START_NEW_GAME)) {

            }

        }

        // main game
        while ()
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