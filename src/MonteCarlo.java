import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarlo {
    private static final int NUM_SIMULATIONS = 1500;

    private static List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getArrayCell()[i][j].getValue() == Cell.Empty_value) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    private static boolean hasWon(Board board, String player) {
        for (int i = 0; i < board.getBoardSize(); i++) {
            if (board.getArrayCell()[i][0].getValue() == player && board.getArrayCell()[i][1].getValue() == player && board.getArrayCell()[i][2].getValue() == player) {
                return true;
            }
            if (board.getArrayCell()[0][i].getValue() == player && board.getArrayCell()[1][i].getValue() == player && board.getArrayCell()[2][i].getValue() == player) {
                return true;
            }
        }

        if (board.getArrayCell()[0][0].getValue() == player && board.getArrayCell()[1][1].getValue() == player && board.getArrayCell()[2][2].getValue() == player) {
            return true;
        }
        if (board.getArrayCell()[0][2].getValue() == player && board.getArrayCell()[1][1].getValue() == player && board.getArrayCell()[2][0].getValue() == player) {
            return true;
        }

        return false;
    }

    private static int getGameResult(Board board) {
        if (hasWon(board, "X")) {
            return 2;
        }
        if (hasWon(board, "O")) {
            return 1;
        }
        return 0;  // Game is a draw
    }

    private static boolean isGameOver(Board board) {
        if (hasWon(board, "X")) {
            return true;
        }
        if (hasWon(board, "O")) {
            return true;
        }

        //kiểm tra bàn cờ con ô trống không
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getArrayCell()[i][j].getValue() == Cell.Empty_value) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void makeMove(Board board, int[] move, String currentPlayer) {
        int row = move[0];
        int col = move[1];
        String player = (currentPlayer.equals(Cell.O_Value)) ? Cell.X_Value : Cell.O_Value;
        board.markPos(row,col,player);
    }

    private static int playGame(Board board, String currentPlayer, List<int[]> moves) {
        List<int[]> clonemoves = new ArrayList<>(moves);  // Make a copy of the moves list

        while (true) {
            // Kiểm tra đã kết thúc game chưa
            if (isGameOver(board)) {
                return getGameResult(board);
            }

            // Chọn ngẫu nhiên ô cần đánh
            Random random = ThreadLocalRandom.current();
            int randomIndex = random.nextInt(clonemoves.size());
            int[] randomMove = clonemoves.get(randomIndex);
            makeMove(board, randomMove, currentPlayer);
            clonemoves.remove(randomIndex);

            // Đổi người chơi
            currentPlayer = (currentPlayer.equals(Cell.O_Value)) ? Cell.X_Value : Cell.O_Value;
        }
    }

    public static int[] findBestMove(Board board, String currentPlayer) {
        long startTime = System.currentTimeMillis();

        Map<int[], int[]> moveResults = new HashMap<>();
        List<int[]> moves = getPossibleMoves(board);
        if (moves.isEmpty()) {
            // Nếu như tất cả ô trên bàn cờ đều đã được đánh thì trả về kết quả null
            return null;
        } else {
            for (int j = 0; j < moves.size(); j++) {
                moveResults.put(moves.get(j),new int[3]);
            }
        }

        // Chạy mô phỏng thuật toán monte carlo
        for (int i = 0; i < NUM_SIMULATIONS; i++) {
            // sao chép bàn cờ
            Board simBoard = board.copyBoard();

            // chọn ô ngẫu nhiên

            Random random = ThreadLocalRandom.current();
            int randomIndex = random.nextInt(moves.size());
            int[] randomMove = moves.get(randomIndex);

            // thực hiện mô phỏng và lấy kết quả
            List<int[]> simulationMoves = new ArrayList<>(moves);
            int result = playGame(simBoard, currentPlayer, simulationMoves);

            // cập nhật kết quả của bước đi đó vào Map moveResults
            int[] results = moveResults.getOrDefault(randomMove, new int[3]);
            results[result]++;
//            System.out.println(randomMove[0] + " " + randomMove[1] + " " + result  + " " + results[result]);
            moveResults.put(randomMove, results);
            simulationMoves.clear();
        }

        // Tìm bước i tốt nhắt
        int[] bestMove = null;
//        int[] bestMove = (moveResults.isEmpty()) ? null : moveResults.entrySet().iterator().next().getKey();
        int maxScore = Integer.MIN_VALUE;
        for (Map.Entry<int[], int[]> entry : moveResults.entrySet()) {
            int[] move = entry.getKey();
            int[] results = entry.getValue();
            int wins = results[1];  // Player 1 wins
            int losses = results[2];  // Player 2 wins
            int ties = results[0];

            // Tính điểm
            int score = (wins * 2) + ties - losses;

            if (score > maxScore) {
                bestMove = move;
                maxScore = score;
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Thời gian tính toán: " + elapsedTime + " ms");
        System.out.println(bestMove[0] + " " + bestMove[1]);
        return bestMove;
    }
}
