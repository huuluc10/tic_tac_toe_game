import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Minimax {
    Minimax() {

    }
    public static int minimax(Board board, int depth, int alpha, int beta, boolean isMax) {
        int boardValue =  evaluateBoard(board, depth);

        if (Math.abs(boardValue) > 0 || board.getAvailableMove() <= 0) {
            return boardValue;
        }

        if (isMax) {
            int hightestValue = Integer.MIN_VALUE;
            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (board.getC()[i][j].getValue().equals(Cell.Empty_value)) {
                        board.markPos(i,j,Cell.O_Value);
                        hightestValue = max(hightestValue, minimax(board, depth - 1, alpha, beta, false));
                        board.Undo(i,j);
                    }
                }
            }
            return hightestValue;
        } else {
            int lowestVal = Integer.MAX_VALUE;
            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (board.getC()[i][j].getValue().equals(Cell.Empty_value)) {
                        board.markPos(i,j,Cell.X_Value);
                        lowestVal = min(lowestVal, minimax(board, depth - 1, alpha, beta, true));
                        board.Undo(i,j);
                    }
                }
            }
            return lowestVal;
        }
    }

    public static int[] getBestMove(Board board) {
        int boardSize = board.getBoardSize();
        int[] bestMove = new int[2];
        int bestValue = Integer.MIN_VALUE;
        //gọi hàm minimax để tìm bước đi tốt nhất
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board.getC()[i][j].getValue().equals(Cell.Empty_value)) {
                    board.markPos(i,j,Cell.O_Value);
                    int moveValue = minimax(board, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, false);;
                    board.getC()[i][j].setValue(Cell.Empty_value);
                    board.Undo(i,j);
                    if(moveValue > bestValue) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        System.out.println(i+" "+j);
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    private static boolean checkHorzontal(Board board, int col, int row, int countWin) {    //kiểm tra theo chiều ngang
        int countLeft = 0;
        int countRight = -1;
        for (int i = col; i >= 0 ; i--) {
            if (board.getC()[col][row].getValue().equals(board.getC()[i][row].getValue())) {
                countLeft++;
            } else {
                break;
            }
        }
        for (int i = col; i < board.getBoardSize(); i++) {
            if (board.getC()[col][row].getValue().equals(board.getC()[i][row].getValue())) {
                countRight++;
                if (countLeft + countRight == countWin) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    private static boolean checkVertical(Board board, int col, int row, int countWin) {     //kiểm tra theo chiều dọc
        int countTop = 0;
        int countBottom = -1;
        for (int i = col; i >= 0 ; i--) {
            if (board.getC()[col][row].getValue().equals(board.getC()[col][i].getValue())) {
                countTop++;
            } else {
                break;
            }
        }
        for (int i = col; i < board.getBoardSize() ; i++) {
            if (board.getC()[col][row].getValue().equals(board.getC()[col][i].getValue())) {
                countBottom++;
                if (countTop + countBottom == countWin) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    private static boolean checkPrimaryDiagonal(Board board, int col, int row, int countWin) {      //kiểm tra theo đường chéo chính
        int countTop = 0;
        int countBottom = -1;
        for (int i = 0; i <= col; i++) {
            if(col - i < 0 || row - i < 0) {
                break;
            }
            if (board.getC()[col][row].getValue().equals(board.getC()[col-i][row-i].getValue())) {
                countTop++;
            }
        }
        for (int i = 0; i < board.getBoardSize() - col; i++) {
            if(col + i > board.getBoardSize() - 1 || row + i > board.getBoardSize() - 1) {
                break;
            }
            if (board.getC()[col][row].getValue().equals(board.getC()[col+i][row+i].getValue())) {
                countBottom++;
                if (countTop + countBottom == countWin) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkSecondaryDiagonal(Board board, int col, int row, int countWin) {    //kiểm tra đường chéo phụ
        int countTop = 0;
        int countBottom = -1;
        for (int i = 0; i <= col; i++) {
            if(col - i < 0 || row + i > board.getBoardSize() - 1) {
                break;
            }
            if (board.getC()[col][row].getValue().equals(board.getC()[col-i][row+i].getValue())) {
                countBottom++;
            }
        }
        for (int i = 0; i < board.getBoardSize() - col; i++) {
            if(col + i > board.getBoardSize() - 1 || row - i < 0) {
                break;
            }
            if (board.getC()[col][row].getValue().equals(board.getC()[col+i][row-i].getValue())) {
                countTop++;
                if (countTop + countBottom == countWin) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int evaluateBoard(Board board, int depth) {
//        int countWin;
//        if(board.getBoardSize() < 5) {
//            countWin = 3;
//        } else {
//            countWin = 5;
//        }
//        boolean rs =  checkHorzontal(board, col, row, countWin) || checkVertical(board, col, row, countWin) || checkPrimaryDiagonal(board, col, row, countWin) || checkSecondaryDiagonal(board, col, row, countWin);
//        if(rs) {
//            if(board.getC()[row][col].getValue().equals(Cell.X_Value)) {
//                return -10;
//            } else {
//                return 10;
//            }
//        }

        for (int i = 0; i < board.getBoardSize(); i++) {
            if(board.getC()[i][0].getValue() == board.getC()[i][1].getValue() && board.getC()[i][0].getValue() == board.getC()[i][2].getValue() && board.getC()[i][0].getValue() != "") {
                if (board.getC()[i][0].getValue() == Cell.O_Value) {
                    return 10 + depth;
                } else {
                    return -10 - depth;
                }
            }
            if(board.getC()[0][i].getValue() == board.getC()[1][i].getValue() && board.getC()[0][i].getValue() == board.getC()[2][i].getValue() && board.getC()[0][i].getValue() != "") {
                if (board.getC()[0][i].getValue() == Cell.O_Value) {
                    return 10 + depth;
                } else {
                    return -10 - depth;
                }
            }
        }
        if (board.getC()[0][0].getValue() == board.getC()[1][1].getValue() && board.getC()[0][0].getValue() == board.getC()[2][2].getValue() && board.getC()[0][0].getValue() != "") {
            if (board.getC()[0][0].getValue() == Cell.O_Value) {
                return 10 + depth;
            } else {
                return -10 - depth;
            }
        }
        if (board.getC()[0][2].getValue() == board.getC()[1][1].getValue() && board.getC()[0][0].getValue() == board.getC()[2][0].getValue() && board.getC()[0][2].getValue() != "") {
            if (board.getC()[2][0].getValue() == Cell.O_Value) {
                return 10 + depth;
            } else {
                return -10 - depth;
            }
        }

        return 0;

    }

}
