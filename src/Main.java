import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.ClassLoader;

public class Main {
    public static void main(String[] args) {

        try {
            // Tải file âm thanh vào bộ nhớ
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/sound.wav"));

            // Tạo đối tượng Clip
            Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioInputStream.getFormat()));

            // Mở clip
            clip.open(audioInputStream);

            // Đặt clip về vị trí bắt đầu
            clip.setFramePosition(0);

            // Phát clip song song
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        int sizeJframe = 700;
        int boardSize = 3;
        boolean AIMode = false;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        Board board = new Board(boardSize, sizeJframe);
        board.selectMode();

        //GUI
        JFrame jFrame = new JFrame("Tic-tac-toe Game");
        jFrame.setSize(sizeJframe+15,sizeJframe+40);
        jFrame.setResizable(false);
        jFrame.add(board);

        int x = ((int) dimension.getWidth()/2 - jFrame.getWidth()/2);
        int y = ((int) dimension.getHeight()/2 - jFrame.getHeight()/2);
        jFrame.setLocation(x,y);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
