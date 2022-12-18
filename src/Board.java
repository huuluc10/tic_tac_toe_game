import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {
    private final int boardSize;
    private final int sizeJframe;
    private final Color newColor = new Color(0xB4BEFD);
    private final int size;
    private Image imgX;
    private Image imgO;
    private Cell[][] c;
    private Image img;
    private int availableMove;
    private final int humanWin = -10;
    private final int aiWin = 10;
    private final int tie = 0;
    private int result = -1;
    private String current_Player = Cell.X_Value;

    public int getBoardSize() {
        return boardSize;
    }
    public void setC(Cell[][] c) {
        this.c = c;
    }
    public Cell[][] getC() {
        return c;
    }
    public int getAvailableMove() {
        return availableMove;
    }

    public Board(int boardSize, int sizeJframe) {

        this.boardSize = boardSize;     //kích thươc bàn cờ
        this.sizeJframe = sizeJframe;   //kích thước Jframe
        availableMove = boardSize * boardSize;      //số nước đi có thể đi
        size = (int) sizeJframe/boardSize;      //kích thước của cell

        c = new Cell[boardSize][boardSize];     //tạo mảng Cell để lưu thông tin bàn cờ
        this.initMatrix();      //khởi tạo mảng Cell rỗng
//        for (int i = 0; i < boardSize; i++) {
//            for (int j = 0; j < boardSize; j++) {
//                if (j == boardSize - 1) {
//                    System.out.println("(" + c[i][j].getX() + ";" + c[i][j].getY() + " (" + i + ";" + j + ") (" + (c[i][j].getX() + c[i][j].getWidth()) + ";" + (c[i][j].getY() + c[i][j].getWidth()) + ")");
//                } else {
//                    System.out.print("(" + c[i][j].getX() + ";" + c[i][j].getY() + " (" + i + ";" + j + ") (" + (c[i][j].getX() + c[i][j].getWidth()) + ";" + (c[i][j].getY() + c[i][j].getWidth()) + ") | ");
//                }
//            }
//        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //lấy tọa đọ chuột click
                int xM = e.getX();
                int yM = e.getY();
                getHumanMove(xM, yM);
            }
        });
        try {
            imgX = ImageIO.read(getClass().getResource("X.png"));
            imgO = ImageIO.read(getClass().getResource("O.png"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initMatrix(){
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                c[i][j] =  new Cell();

                int x = i * size;
                int y = j * size;

                c[i][j].setX(y);
                c[i][j].setY(x);
                c[i][j].setWidth(size-1);
                c[i][j].setHeight(size-1);
                c[i][j].setValue("");
            }
        }
    }

    private boolean checkHorzontal(int col, int row, int countWin) {    //kiểm tra theo chiều ngang
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

    private boolean checkVertical(int col, int row, int countWin) {     //kiểm tra theo chiều dọc
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

    private boolean checkPrimaryDiagonal(int col, int row, int countWin) {      //kiểm tra theo đường chéo chính
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

    private boolean checkSecondaryDiagonal(int col, int row, int countWin) {    //kiểm tra đường chéo phụ
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

    private int CheckWin(int col, int row, String value){
//        int countWin = 0;
//        if(boardSize < 5) {
//            countWin = 3;
//        } else {
//            countWin = 5;
//        }
//        System.out.println(availableMove);
//        boolean rs =  checkHorzontal(col, row, countWin) || checkVertical(col, row, countWin) || checkPrimaryDiagonal(col, row, countWin) || checkSecondaryDiagonal(col, row, countWin);
//        if(rs == true) {
//            if(value.equals(Cell.X_Value)) {
//                return humanWin;
//            } else {
//                return aiWin;
//            }
//        }
//        if(availableMove <= 0) {
//            return tie;
//        }
//        return -1;
        if(availableMove <= 0) {
            return tie;
        }
        for (int i = 0; i < 3; i++) {
            if(c[i][0].getValue() == c[i][1].getValue() && c[i][0].getValue() == c[i][2].getValue() && c[i][0].getValue() != "") {
                if(c[i][0].getValue().equals(Cell.X_Value)) {
                    return humanWin;
                } else {
                    return aiWin;
                }
            }
            if(c[0][i].getValue() == c[1][i].getValue() && c[0][i].getValue() == c[2][i].getValue() && c[0][i].getValue() != "") {
                if(c[0][i].getValue().equals(Cell.X_Value)) {
                    return humanWin;
                } else {
                    return aiWin;
                }
            }
        }
        if (c[0][0].getValue() == c[1][1].getValue() && c[0][0].getValue() == c[2][2].getValue() && c[0][0].getValue() != "") {
            if(c[0][0].getValue().equals(Cell.X_Value)) {
                return humanWin;
            } else {
                return aiWin;
            }
        }
        if (c[0][2].getValue() == c[1][1].getValue() && c[0][2].getValue() ==c [2][0].getValue() && c[0][2].getValue() != "") {
            if(c[0][2].getValue().equals(Cell.X_Value)) {
                return humanWin;
            } else {
                return aiWin;
            }
        }
        return -1;
    }

    private void EndGame(int col, int row, String value) {
        String playerWin ="";
        int result = CheckWin(col,row,value);
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
                availableMove = boardSize * boardSize;
                initMatrix();
                repaint();
                current_Player = Cell.X_Value;
            } else {
                System.exit(0);
            }
        }
    }

    private void PlayAI() {
        if (current_Player.equals(Cell.O_Value)) {
            //Gọi minimax trả về kiểu dữ liệu Cell để lấy tọa độ X, Y
            Board board_copy = this;
            int x , y;
            int [] coordinates = Minimax.getBestMove(board_copy);
            x = coordinates[0];
            y = coordinates[1];
            System.out.println(x+" "+y);
            c[x][y].setValue(Cell.O_Value);
            availableMove--;
            EndGame(x,y,c[x][y].getValue());
            repaint();
            current_Player = Cell.X_Value;
        }
    }

    private void getHumanMove (int xM, int yM) {
        System.out.println(xM + " " + yM);
        //tính toán nhân vào cell nào
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Cell cell = c[i][j];
                int cX_Start = cell.getX();
                int cY_Start = cell.getY();
                int cX_End = cell.getX() + cell.getWidth();
                int cY_End = cell.getY() + cell.getHeight();

                if (cX_Start <= xM && cX_End >= xM && cY_Start <= yM && cY_End >= yM) {
                    System.out.println("Đã chọn ô ở hàng " + (i+1) + " cột " + (j+1));

                    if(c[i][j].getValue() == "") {
                        c[i][j].setValue(Cell.X_Value);
                        repaint();
                        availableMove--;
                        EndGame(i,j,c[i][j].getValue());
                        current_Player = Cell.O_Value;
                        PlayAI();
                        printMatrix();
                    }
                    //kết thúc vòng lặp
                    i = boardSize-1;
                    break;
                }
            }
        }
    }

    public void printMatrix() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (j == boardSize - 1) {
                    if (c[i][j].getValue() != " "){
                        System.out.println(c[i][j].getValue());
                    } else {
                        System.out.println(" ");
                    }
                } else {
                    System.out.print(c[i][j].getValue() + " | ");
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphic2d = (Graphics2D) g;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Color color = newColor;
                graphic2d.setColor(color);

                graphic2d.fillRect(c[i][j].getX(),c[i][j].getY(),c[i][j].getWidth() - 1,c[i][j].getHeight() - 1);

                if (c[i][j].getValue().equals(Cell.X_Value)) {
                    img = imgX;
                    graphic2d.drawImage(img, c[i][j].getX(),c[i][j].getY(),c[i][j].getWidth(),c[i][j].getHeight(), this);
                } else if(c[i][j].getValue().equals(Cell.O_Value)) {
                    img = imgO;
                    graphic2d.drawImage(img, c[i][j].getX(),c[i][j].getY(),c[i][j].getWidth(),c[i][j].getHeight(), this);
                }
                else {
                    img = null;
                    graphic2d.drawImage(img, c[i][j].getX(),c[i][j].getY(),c[i][j].getWidth(),c[i][j].getHeight(), this);
                }
            }
        }
    }

}
