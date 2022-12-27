import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Minimax {
    private static final int depth = 9;
    Minimax() {

    }
    public static int minimax(Board board, int depth, int alpha, int beta, boolean isMax) {
        int boardValue =  evaluateBoard(board, depth);

        if (Math.abs(boardValue) > 0 || board.getAvailableMove() <= 0 || depth == 0) {
            return boardValue;
        }

        if (isMax) {
            int hightestValue = Integer.MIN_VALUE;
            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (board.getArrayCell()[i][j].getValue().equals(Cell.Empty_value)) {
                        board.markPos(i,j,Cell.O_Value);
                        hightestValue = max(hightestValue, minimax(board, depth - 1, alpha, beta, false));
                        board.Undo(i,j);
                        alpha = Math.max(alpha, hightestValue);
                        if (alpha >= beta) {
                            return hightestValue;
                        }
                    }
                }
            }
            return hightestValue;
        } else {
            int lowestVal = Integer.MAX_VALUE;
            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (board.getArrayCell()[i][j].getValue().equals(Cell.Empty_value)) {
                        board.markPos(i,j,Cell.X_Value);
                        lowestVal = min(lowestVal, minimax(board, depth - 1, alpha, beta, true));
                        board.Undo(i,j);
                        beta = Math.min(beta, lowestVal);
                        if (beta <= alpha) {
                            return lowestVal;
                        }
                    }
                }
            }
            return lowestVal;
        }
    }

    public static int[] getBestMove(Board board) {
        long startTime = System.currentTimeMillis();
        List<int[]> emptySpots = new ArrayList<>();

        // Tìm các ô trống trên bàn cờ
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getArrayCell()[i][j].getValue().equals(Cell.Empty_value)) {
                    emptySpots.add(new int[] {i, j});
                }
            }
        }

        // Mảng chứa kết quả số lần thắng của máy tại mỗi ô trống
        int[] results = new int[emptySpots.size()];
        int boardSize = board.getBoardSize();
        int[] bestMove = new int[2];

        int bestValue = Integer.MIN_VALUE;
        //gọi hàm minimax để tìm bước đi tốt nhất
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board.getArrayCell()[i][j].getValue().equals(Cell.Empty_value)) {
                    board.markPos(i,j,Cell.O_Value);
                    int moveValue = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);;
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
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Thời gian tính toán: " + elapsedTime + " ms");
        return bestMove;
    }

    private static int evaluateBoard(Board board, int depth) {
        for (int i = 0; i < board.getBoardSize(); i++) {
            if(board.getArrayCell()[i][0].getValue() == board.getArrayCell()[i][1].getValue() && board.getArrayCell()[i][0].getValue() == board.getArrayCell()[i][2].getValue() && board.getArrayCell()[i][0].getValue() != "") {
                if (board.getArrayCell()[i][0].getValue() == Cell.O_Value) {
                    return 10 + depth;
                } else {
                    return -10 - depth;
                }
            }
            if(board.getArrayCell()[0][i].getValue() == board.getArrayCell()[1][i].getValue() && board.getArrayCell()[0][i].getValue() == board.getArrayCell()[2][i].getValue() && board.getArrayCell()[0][i].getValue() != "") {
                if (board.getArrayCell()[0][i].getValue() == Cell.O_Value) {
                    return 10 + depth;
                } else {
                    return -10 - depth;
                }
            }
        }
        if (board.getArrayCell()[0][0].getValue() == board.getArrayCell()[1][1].getValue() && board.getArrayCell()[0][0].getValue() == board.getArrayCell()[2][2].getValue() && board.getArrayCell()[0][0].getValue() != "") {
            if (board.getArrayCell()[0][0].getValue() == Cell.O_Value) {
                return 10 + depth;
            } else {
                return -10 - depth;
            }
        }
        if (board.getArrayCell()[0][2].getValue() == board.getArrayCell()[1][1].getValue() && board.getArrayCell()[0][0].getValue() == board.getArrayCell()[2][0].getValue() && board.getArrayCell()[0][2].getValue() != "") {
            if (board.getArrayCell()[2][0].getValue() == Cell.O_Value) {
                return 10 + depth;
            } else {
                return -10 - depth;
            }
        }

        return 0;
    }
}