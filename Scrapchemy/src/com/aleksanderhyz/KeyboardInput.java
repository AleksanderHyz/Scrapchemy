package com.aleksanderhyz;

import java.util.Scanner;

/**
 *  Class for defining methods for commands sent by Player from keyboard
 *
 *  command tree:
 *  MainGameChoice
 *      GO_TO_MARKET
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

    private Scanner scanner = new Scanner(System.in);

    // operations after game start
    public enum StartGameChoice {
        START_NEW_GAME("START"),
        LOAD_GAME("LOAD"),
        QUIT("QUIT"),
        WRONG_COMMAND(null);

        private String command;

        public String getCommand() {
            return command;
        }

        StartGameChoice (String command) {
            this.command = command;
        }

        public static StartGameChoice getChoiceByCommand (String command) {
            for (StartGameChoice startGameChoice : values()) {
                if (command.equals(startGameChoice.getCommand())) {
                    return startGameChoice;
                }
            }
            return WRONG_COMMAND;
        }
    }
    public StartGameChoice startGameChoice () {
        String command = scanner.nextLine().toUpperCase();
        return StartGameChoice.getChoiceByCommand(command);

    }

    // operations during the game
    public enum MainGameChoice {
        GO_TO_MARKET("MARKET"),
        GO_TO_INVENTORY("INVENTORY"),
        GO_TO_COMMISSIONS("COMMISSIONS"),
        PAUSE_GAME("PAUSE"),
        WRONG_COMMAND(null);

        private String command;

        public String getCommand() {
            return command;
        }

        MainGameChoice(String command) {
            this.command = command;
        }

        public static MainGameChoice getChoiceByCommand (String command) {
            for (MainGameChoice mainGameChoice : values()) {
                if (command.equals(mainGameChoice.getCommand())) {
                    return mainGameChoice;
                }
            }
            return WRONG_COMMAND;
        }
    }
    public MainGameChoice mainGameChoice () {
        String command = scanner.nextLine().toUpperCase();
        return MainGameChoice.getChoiceByCommand(command);

    }

}
