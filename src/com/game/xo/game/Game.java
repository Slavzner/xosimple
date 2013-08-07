package com.game.xo.game;

import com.game.xo.players.*;

import java.util.Scanner;

public class Game {
    public final int MAX_STEPS = 9;
    private final char EMPTY_CELL = ' ';
    private final char SYMBOL_X = 'x';
    private final char SYMBOL_0 = '0';
    private int globalStepCount = 0;
    private static int axisX;
    private static int axisY;
    private int findWinner;
    private final int SIZE = 3;
    private final int MAX_INDEX = 2;
    private final int MIN_INDEX = 0;

    private Scanner in = new Scanner(System.in);

    private Field field;

    private enum GameMode {
        HUMAN, COMPUTER, ERROR
    }

    private Human firstPlayer;
    private Player secondPlayer;


    public void createGame() {
        boolean flagError = true;//false when no errors
        boolean endOfGameFlag = true;//false if exit from game
        String continueGame;
        String namePlayer;
        String chosenSymbol = "N/A";
        String tempOption;
        GameMode mode;

        while (endOfGameFlag) {
            field = new Field();

            displayMessage("Field start.\n");

            displayMessage("Enter first name: ");
            namePlayer = in.nextLine();
            firstPlayer = new Human(namePlayer);
            //secondPlayer = new Player();

            do {
                displayMessage("You play with human or computer? h/c: ");
                tempOption = in.nextLine();
                mode = chooseMode(tempOption);
                switch (mode) {
                    case HUMAN:
                        displayMessage("Enter second name: ");
                        namePlayer = in.nextLine();
                        secondPlayer = new Human(namePlayer);
                        break;
                    case COMPUTER:
                        secondPlayer = new Computer();
                        break;
                    default:
                        displayMessage("Invalid option!!! Try again.");
                        flagError = false;
                        break;
                }
            } while (!flagError);
            field.displayField();
            flagError = true;

            do {
                displayMessage(firstPlayer.getName() + "\nChoose your symbol x or 0 : ");
                System.out.println(chosenSymbol.charAt(0));
                chosenSymbol = in.nextLine();
                System.out.println(chosenSymbol.charAt(0));
                //add - check if chosen x/X or 0.
                switch (chosenSymbol.charAt(0)) {
                    case SYMBOL_0:
                        setSymbol(firstPlayer, secondPlayer);
                        flagError = true;
                        break;
                    case SYMBOL_X:
                        setSymbol(secondPlayer, firstPlayer);
                        flagError = true;
                        break;
                    default:
                        displayMessage("Invalid option!!! Try again.");
                        flagError = false;
                        break;
                }
            } while (!flagError);
            while (!firstPlayer.getYouWin() && !secondPlayer.getYouWin() && firstPlayer.getPlayerSteps() < MAX_STEPS) {
                displayMessage(firstPlayer.getName() + " your turn. Your symbol: " + firstPlayer.getPlayerSymbol());
                gameMovies(firstPlayer, flagError, GameMode.HUMAN);

                field.displayField();
                if (firstPlayer.getYouWin()) {
                    break;
                }
                if (firstPlayer.getPlayerSteps() >= MAX_STEPS) {
                    displayMessage("\nStandoff");
                    break;
                }
                displayMessage(secondPlayer.getName() + " your turn. Your symbol: " + secondPlayer.getPlayerSymbol());
                gameMovies(secondPlayer, flagError, mode);
                field.displayField();

            }

            displayMessage("Start the new game? y/n: ");
            continueGame = in.nextLine();

            flagError = false;
            while (!flagError) {
                if (continueGame.charAt(0) == 'y' || continueGame.charAt(0) == 'Y') {
                    endOfGameFlag = true;
                    globalStepCount = 0;
                } else {
                    if (continueGame.charAt(0) == 'n' || continueGame.charAt(0) == 'N') {
                        endOfGameFlag = false;
                    } else {
                        displayMessage("Invalid option!!! Try again.");

                    }
                }
                flagError = true;
            }

        }

        displayMessage("\nEnd of game...");
        in.nextLine();
    }

    public void gameMovies(Player player, boolean flagError, GameMode mode) {
        while (flagError && !player.getYouWin()) {
            flagError = getPlayerStep(mode);
            flagError = checkWin(player, flagError);
        }


    }

    private boolean getPlayerStep(GameMode mode) {

        switch (mode) {
            case HUMAN:
                System.out.print("Enter coordinate (x, ): ");
                axisX = in.nextInt();
                System.out.print("Enter coordinate ( ,y): ");
                axisY = in.nextInt();
                if (axisX >= MIN_INDEX && axisX <= MAX_INDEX && axisY >= MIN_INDEX && axisY <= MAX_INDEX) {
                    return true;
                } else {
                    return false;
                }
                //break;
            case COMPUTER:
                Computer computer = (Computer) secondPlayer;
                if (computer.setFirstSymbol(SIZE, axisX, axisY)) {
                    axisX = computer.getPcX();
                    axisY = computer.getPcY();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean checkWin(Player player, boolean flagError) {
        if (!searchWinner(player.getPlayerSymbol())) {
            if (field.getGameField(axisX, axisY) == EMPTY_CELL) {
                field.setGameField(axisX, axisY, player.getPlayerSymbol());
                flagError = update(flagError, player);
            } else {
                System.out.println("Your coordinates invalid.\nPlease enter correct coordinates.");
                field.displayField();
                return true;
            }
        } else {
            System.out.println("\n" + player.getName() + " you win!!!\n");
            player.setYouWin(true);
            flagError = false;
        }
        return flagError;
    }

    private boolean update(boolean flagError, Player player) {
        if (flagError) {
            globalStepCount++;
            player.setPlayerSteps(globalStepCount);
        }
        flagError = false;
        return flagError;

    }

    private void setSymbol(Player player1, Player player2) {
        player1.setPlayerSymbol(SYMBOL_0);
        player2.setPlayerSymbol(SYMBOL_X);

    }

    private void displayMessage(String string) {
        System.out.println(string);
    }

    private GameMode chooseMode(String chosen) {
        if (chosen.charAt(0) == 'h' || chosen.charAt(0) == 'H') {
            return GameMode.HUMAN;
        } else {
            if (chosen.charAt(0) == 'c' || chosen.charAt(0) == 'C') {
                return GameMode.COMPUTER;
            }
        }
        return GameMode.ERROR;
    }

    public boolean searchWinner(char symbol) {
        if (rowWinner(symbol) || columnWinner(symbol) || diagonalWinner(symbol)) {
            return true;
        } else {
            return false;
        }
    }

    /*search winner in rows*/
    private boolean rowWinner(char symbol) {

        for (int i = 0; i < SIZE; i++) {
            findWinner = rowWinnerSub(i, symbol);
            if (findWinner == SIZE) {
                return true;
            }
        }
        return false;
    }

    private int rowWinnerSub(int axisX, char symbol) {
        int sum = 0;
        for (int j = 0; j < SIZE; j++) {
            if (field.getGameField(axisX, j) == symbol) {
                sum++;
            }
        }
        return sum;
    }

    /*search winner in columns*/
    private boolean columnWinner(char symbol) {
        for (int i = 0; i < SIZE; i++) {
            findWinner = columnWinnerSub(i, symbol);
            if (findWinner == SIZE) {
                return true;
            }
        }
        return false;
    }

    private int columnWinnerSub(int axisY, char symbol) {
        int sum = 0;
        for (int j = 0; j < SIZE; j++) {
            if (field.getGameField(j, axisY) == symbol) {
                sum++;
            }
        }
        return sum;
    }

    /*search winner in diagonals*/
    private boolean diagonalWinner(char symbol) {
        int i, sum = 0;
        for (i = 0; i < SIZE; i++) {
            if (field.getGameField(i, i) == symbol) {
                sum++;
            }
        }
        i = 1;
        if (sum == SIZE || field.getGameField(i, i) == symbol && field.getGameField(i + 1, i - 1) == symbol && field.getGameField(i - 1, i + 1) == symbol) {
            return true;
        }
        return false;

    }

}