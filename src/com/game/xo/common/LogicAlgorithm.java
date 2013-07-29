package com.game.xo.common;


public class LogicAlgorithm {
    private char[][] tempField;
    private int findWinner;
    private static final int WINS = 3;


    public boolean searchWinner(char[][] newArray, char symbol) {
        tempField = newArray;
        if (rowWinner(symbol) || columnWinner(symbol) || diagonalWinner(symbol)) {
            return true;
        } else {
            return false;
        }
    }

    /*search winner in rows*/
    private boolean rowWinner(char symbol) {

        for (int i = 0; i < tempField.length; i++) {
            findWinner = rowWinnerSub(i, tempField.length, symbol);
            if (findWinner == WINS) {
                return true;
            }
        }
        return false;
    }

    private int rowWinnerSub(int axisX, int size, char symbol) {
        int sum = 0;
        for (int j = 0; j < size; j++) {
            if (tempField[axisX][j] == symbol) {
                sum++;
            }
        }
        return sum;
    }

    /*search winner in columns*/
    private boolean columnWinner(char symbol) {
        for (int i = 0; i < tempField.length; i++) {
            findWinner = columnWinnerSub(i, tempField.length, symbol);
            if (findWinner == WINS) {
                return true;
            }
        }
        return false;
    }

    private int columnWinnerSub(int axisY, int size, char symbol) {
        int sum = 0;
        for (int j = 0; j < size; j++) {
            if (tempField[j][axisY] == symbol) {
                sum++;
            }
        }
        return sum;
    }

    /*search winner in diagonal*/
    private boolean diagonalWinner(char symbol) {
        int i,sum = 0;
        for (i = 0; i < tempField.length; i++) {
            if (tempField[i][i] == symbol) {
                sum++;
            }
        }
        i = 1;
        if(sum == WINS || tempField[i][i] == symbol && tempField[i+1][i-1] == symbol && tempField[i-1][i+1] == symbol)  {
            return true;
        }
        return false;

    }


}
