import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Minimax {
    private static final int depth = 9;
    Minimax() {

    }
    private static int minimax(Board board, int depth, int alpha, int beta, boolean isMax) {
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
        long startTime = System.nanoTime();

        // M???ng ch???a k???t qu??? s??? l???n th???ng c???a m??y t???i m???i ?? tr???ng
        int boardSize = board.getBoardSize();
        int[] bestMove = new int[2];

        int bestValue = Integer.MIN_VALUE;
        //g???i h??m minimax ????? t??m b?????c ??i t???t nh???t
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board.getArrayCell()[i][j].getValue().equals(Cell.Empty_value)) {
                    board.markPos(i,j,Cell.O_Value);
                    int moveValue = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);;
                    board.Undo(i,j);
                    if(moveValue > bestValue) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestValue = moveValue;
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime);
        System.out.println("Th???i gian t??nh to??n: " + elapsedTime + " ns");
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
        // ???????ng ch??o ch??nh
        if (board.getArrayCell()[0][0].getValue() == board.getArrayCell()[1][1].getValue() && board.getArrayCell()[0][0].getValue() == board.getArrayCell()[2][2].getValue() && board.getArrayCell()[0][0].getValue() != "") {
            if (board.getArrayCell()[0][0].getValue() == Cell.O_Value) {
                return 10 + depth;
            } else {
                return -10 - depth;
            }
        }
        // ???????ng ch??o ph???
        if (board.getArrayCell()[0][2].getValue() == board.getArrayCell()[1][1].getValue() && board.getArrayCell()[0][2].getValue() == board.getArrayCell()[2][0].getValue() && board.getArrayCell()[0][2].getValue() != "") {
            if (board.getArrayCell()[0][2].getValue() == Cell.O_Value) {
                return 10 + depth;
            } else {
                return -10 - depth;
            }
        }

        return 0;
    }
}