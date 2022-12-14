public class Minimax {
    Minimax() {

    }
    public static int minimax(Board board, boolean isMax, int col, int row) {
        int boardValue =  evaluateBoard(board.getC(), board.getBoardSize(),col,row);

        return 0;
    }

    public static Cell getBestMove(Board board) {
        Cell[][] c = board.getC();
        Cell c1 = board.getC()[1][0];
        int boardSize = board.getBoardSize();
        Cell bestMove = new Cell();
        int bestValue = Integer.MIN_VALUE;
        //gọi hàm minimax để tìm bước đi tốt nhất
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board.getC()[i][j].getValue().equals("")) {
                    board.getC()[i][j].setValue(Cell.O_Value);
                    board.printMatrix();
                    System.out.println("--------------------------");
                    int moveValue = minimax(board,false, i, j);
                    board.getC()[i][j].setValue(Cell.Empty_value);

                    if(moveValue > bestValue) {
                        bestMove.setX(i);
                        bestMove.setY(j);
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    private static boolean checkHorzontal(Cell[][] c, int boardSize, int col, int row, int countWin) {    //kiểm tra theo chiều ngang
        int countLeft = 0;
        int countRight = -1;
        for (int i = col; i >= 0 ; i--) {
            if (c[col][row].getValue() == c[i][row].getValue()) {
                countLeft++;
            } else {
                break;
            }
        }
        for (int i = col; i < boardSize; i++) {
            if (c[col][row].getValue() == c[i][row].getValue()) {
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

    private static boolean checkVertical(Cell[][] c, int boardSize, int col, int row, int countWin) {     //kiểm tra theo chiều dọc
        int countTop = 0;
        int countBottom = -1;
        for (int i = col; i >= 0 ; i--) {
            if (c[col][row].getValue() == c[col][i].getValue()) {
                countTop++;
            } else {
                break;
            }
        }
        for (int i = col; i < boardSize ; i++) {
            if (c[col][row].getValue() == c[col][i].getValue()) {
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

    private static boolean checkPrimaryDiagonal(Cell[][] c, int boardSize, int col, int row, int countWin) {      //kiểm tra theo đường chéo chính
        int countTop = 0;
        int countBottom = -1;
        for (int i = 0; i <= col; i++) {
            if(col - i < 0 || row - i < 0) {
                break;
            }
            if (c[col][row].getValue() == c[col-i][row-i].getValue()) {
                countTop++;
            }
        }
        for (int i = 0; i < boardSize - col; i++) {
            if(col + i > boardSize - 1 || row + i > boardSize - 1) {
                break;
            }
            if (c[col][row].getValue() == c[col+i][row+i].getValue()) {
                countBottom++;
                if (countTop + countBottom == countWin) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkSecondaryDiagonal(Cell[][] c, int boardSize, int col, int row, int countWin) {    //kiểm tra đường chéo phụ
        int countTop = 0;
        int countBottom = -1;
        for (int i = 0; i <= col; i++) {
            if(col - i < 0 || row + i > boardSize - 1) {
                break;
            }
            if (c[col][row].getValue() == c[col-i][row+i].getValue()) {
                countBottom++;
            }
        }
        for (int i = 0; i < boardSize - col; i++) {
            if(col + i > boardSize - 1 || row - i < 0) {
                break;
            }
            if (c[col][row].getValue() == c[col+i][row-i].getValue()) {
                countTop++;
                if (countTop + countBottom == countWin) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int evaluateBoard(Cell[][] cell, int boardSize, int col, int row) {
        int countWin = 0;
        if(boardSize < 5) {
            countWin = 3;
        } else {
            countWin = 5;
        }
        boolean rs =  checkHorzontal(cell, boardSize, col, row, countWin) || checkVertical(cell, boardSize, col, row, countWin) || checkPrimaryDiagonal(cell, boardSize, col, row, countWin) || checkSecondaryDiagonal(cell, boardSize, col, row, countWin);
        if(rs == true) {
            if(cell[col][row].equals(Cell.X_Value)) {
                return 10;
            } else {
                return -10;
            }
        }
        return 0;
    }

}
