package com.aleksanderhyz;

import java.util.Scanner;

/**
 *  Class for defining methods for commands sent by Player from keyboard
 *
 *  command trees:
 *
 *  StarGameChoice:
 *      START_NEW_GAME
 *          [choose name for new Player and start the game]
 *      LOAD_GAME
 *          [chose .db save file to load]
 *      QUIT
 *
 *  MainGameChoice:
 *      GO_TO_MARKET -> MarketChoice
 *          BUY
 *              [choose item to buy]
 *              RETURN
 *          SELL
 *              SELL_ITEM
 *                  [choose item to sell]
 *                  RETURN
 *              SELL_COMPONENT
 *                  [choose component to sell]
 *                  RETURN
 *              SELL_MATERIAL
 *                  [choose material to sell]
 *                      [choose amount if it to sell]
 *                      RETURN
 *                  RETURN
 *              RETURN
 *          RETURN
 *      GO_TO_INVENTORY
 *          DISMANTLE_ITEM
 *              [choose item to dismantle]
 *              RETURN
 *          PROCESS_COMPONENT
 *              [choose component to process]
 *              RETURN
 *          RETURN
 *      GO_TO_COMMISSIONS
 *          [choose commission to fulfill]
 *              [choose ingredients]
 *              RETURN
 *          RETURN
 *      PAUSE_GAME
 *          SAVE_GAME
 *          RESUME_GAME
 *          QUIT_GAME
 */

public class KeyboardInput {

    private static Scanner scanner = new Scanner(System.in);

    // commands for the whole game
        // all commands were moved to one enum (previously there were separate enums for each group)
        // to avoid code repetition (the enum methods)
        // and because the way to recognize commands from different groups as unknown ones was not needed
        // since each command has to recognized separately in Main methods anyway
    public enum GameChoice {
        // start game commands
        START_NEW_GAME("START"),
        LOAD_GAME("LOAD"),
        QUIT("QUIT"),

        // general return command
        RETURN("RETURN"),

        // when unspecified command is given
        UNKNOWN_COMMAND(null),

        // main game commands
        GO_TO_MARKET("MARKET"),
            // market commands
            BUY("BUY"),
            SELL("SELL"),
        GO_TO_INVENTORY("INVENTORY"),
            // inventory commands
            DISMANTLE_ITEM("DISMANTLE"),
            PROCESS_COMPONENT("PROCESS"),
        GO_TO_COMMISSIONS("COMMISSIONS"),
            // commission commands
            CHOOSE_COMMISSION("CHOOSE"),
        PAUSE_GAME("PAUSE"),
            // pause commands
            SAVE_GAME("SAVE"),
            RESUME_GAME("RESUME");

        private String command;

        public String getCommand() {
            return command;
        }

        GameChoice(String command) {
            this.command = command;
        }

        public static GameChoice getChoiceByCommand (String command) {
            for (GameChoice gameChoice : values()) {
                if (command.equals(gameChoice.getCommand())) {
                    return gameChoice;
                }
            }
            return UNKNOWN_COMMAND;
        }
    }
    public static GameChoice gameChoice () {
        String command = scanner.nextLine().toUpperCase();
        return GameChoice.getChoiceByCommand(command);
    }


    // methods used for creating new Player
    public static String enterPlayerName () {
        String playerName;

        playerName = scanner.nextLine();

        return playerName;
    }
}
