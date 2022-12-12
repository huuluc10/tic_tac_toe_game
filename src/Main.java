import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        int sizeJframe = 700;
        int boardSize = 3;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        Board board = new Board(boardSize, sizeJframe);

        //GUI
        JFrame jFrame = new JFrame("Tic-tac-toe Game");
        jFrame.setSize(sizeJframe+15,sizeJframe+40);
        jFrame.setResizable(false);
        jFrame.add(board);
        jFrame.add(board);

        int x = ((int) dimension.getWidth()/2 - jFrame.getWidth()/2);
        int y = ((int) dimension.getHeight()/2 - jFrame.getHeight()/2);
        jFrame.setLocation(x,y);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
