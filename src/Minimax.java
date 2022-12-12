public class Minimax {
    Minimax() {

    }
    public int Minimax(Board board, boolean isMax) {
        return 0;
    }

    public static Cell getBestMove(Cell[][] cell, int boardSize) {
        Cell[][] c = cell;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(c[j][i].getValue() + " | ");
            }
            System.out.println();
        }
        Cell bestMove = new Cell();
        int bestValue = Integer.MIN_VALUE;
        //gọi hàm minimax để tìm bước đi tốt nhất
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

            }
        }
        return bestMove;
    }

}
