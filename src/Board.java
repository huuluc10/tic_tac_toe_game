import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {
    private int boardSize;

    public int getBoardSize() {
        return boardSize;
    }

    private int sizeJframe;
    private Color newColor = new Color(0xB4BEFD);
    private int size;
    private Image imgX;
    private Image imgO;
    private Cell[][] c;
    private String current_Player = Cell.X_Value;
    private String last_Player = "";
    private Image img;
    private int availableMove;
    private final int humanWin = -10;
    private final int aiWin = 10;
    private final int tie = 0;
    private int result = -1;

    public Board(int boardSize, int sizeJframe) {

        this.boardSize = boardSize;
        this.sizeJframe = sizeJframe;
        availableMove = boardSize * boardSize;
        System.out.println(availableMove);
        size = (int) sizeJframe/boardSize;

        c = new Cell[boardSize][boardSize];
        this.initMatrix();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int xM = e.getX();
                int yM = e.getY();


                //tính toán nhân vào cell nào
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        Cell cell = c[i][j];
//                        System.out.println(cell.getX() + ";" + cell.getY());
                        int cX_Start = cell.getX();
                        int cY_Start = cell.getY();
                        int cX_End = cell.getX() + cell.getWidth();
                        int cY_End = cell.getY() + cell.getHeight();

                        if (cX_Start <= xM && cX_End >= xM && cY_Start <= yM && cY_End >= yM) {
                            System.out.println("Đã chọn ô ở cột " + (i+1) + " hàng " + (j+1));

                            if(c[i][j].getValue() == "" || c[i][j].getValue() != last_Player) {
                                c[i][j].setValue(current_Player);
                                last_Player = current_Player;
                                current_Player = current_Player.equals(Cell.X_Value) ? Cell.O_Value : Cell.X_Value;
                                repaint();
                                availableMove--;
                                result = CheckWin(i,j,c[i][j].getValue());
                                checkEndGame();
                            }
                            i = boardSize-1;
                            break;
                        }
                    }
                }
            }
        });

        try {
            imgX = ImageIO.read(getClass().getResource("X.png"));
            imgO = ImageIO.read(getClass().getResource("O.png"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Board(Cell[][] c) {
        this.c = c;
    }

    private void initMatrix(){
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Cell cell = new Cell();
                c[i][j] = cell;
            }
        }
    }

    private boolean checkHorzontal(int col, int row, int countWin) {
        int countLeft = 0;
        int countRight = -1;
        for (int i = col; i >= 0 ; i--) {
            if (c[col][row].getValue() == c[i][row].getValue()) {
                countLeft++;
            } else {
                break;
            }
        }
        for (int i = col; i < boardSize ; i++) {
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

    private boolean checkVertical(int col, int row, int countWin) {
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

    private boolean checkPrimaryDiagonal(int col, int row, int countWin) {
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

    private boolean checkSecondaryDiagonal(int col, int row, int countWin) {
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

    public int CheckWin(int col, int row, String value){
        int countWin = 0;
        if(boardSize < 5) {
            countWin = 3;
        } else {
            countWin = 5;
        }
        boolean rs =  checkHorzontal(col, row, countWin) || checkVertical(col, row, countWin) || checkPrimaryDiagonal(col, row, countWin) || checkSecondaryDiagonal(col, row, countWin);
        if(rs == true) {
            if(value.equals(Cell.X_Value)) {
                return humanWin;
            } else {
                return aiWin;
            }
        }
        if(availableMove <= 0) {
            return tie;
        }
        return -1;
    }

    private void checkEndGame() {
        String playerWin ="";
        if(result == humanWin) {
            playerWin ="Bạn thắng";
        } else if (result == aiWin) {
            playerWin = "Máy thắng";
        } else if(result == tie){
            playerWin = "Hòa";
        }
        if (result != -1) {
            int reply = JOptionPane.showConfirmDialog(null, "Trò chơi kết thúc!\n" + playerWin + "\nBạn có muốn chơi lại?", "Tiếp tục?",  JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION)
            {
                resetGame();
            }
        }
    }

    private void resetGame() {
        availableMove = boardSize * boardSize;
        initMatrix();
        last_Player = "";
        current_Player = Cell.X_Value;
        repaint();
    }

    public boolean isTileMarked(int row, int column) {
//        return board[row][column].isMarked();
        return true;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphic2d = (Graphics2D) g;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Color color = newColor;
                graphic2d.setColor(color);

                int x = i * size;
                int y = j * size;

                c[i][j].setX(x);
                c[i][j].setY(y);
                c[i][j].setWidth(size-1);
                c[i][j].setHeight(size-1);

                graphic2d.fillRect(x,y,size - 1,size - 1);

                if (c[i][j].getValue().equals(Cell.X_Value)) {
                    img = imgX;
                    graphic2d.drawImage(img, c[i][j].getX(),c[i][j].getY(),c[i][j].getWidth(),c[i][j].getHeight(), this);
                } else if(c[i][j].getValue().equals(Cell.O_Value)) {
                    img = imgO;
                    graphic2d.drawImage(img, c[i][j].getX(),c[i][j].getY(),c[i][j].getWidth(),c[i][j].getHeight(), this);
                }

            }
        }
    }

}
