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
    private Cell[][] arrayCell;
    private Image img;
    private int availableMove;
    private String current_Player;
    public String getCurrent_Player() {
        return current_Player;
    }

    public void setCurrent_Player(String current_Player) {
        this.current_Player = current_Player;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Cell[][] getArrayCell() {
        return arrayCell;
    }

    public int getAvailableMove() {
        return availableMove;
    }

    public Board copyBoard() {
        // Create a new board with the same size as this board
        Board copy = new Board(this.boardSize, this.sizeJframe);

        // Copy the cells from this board to the new board
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                copy.getArrayCell()[i][j].setValue(this.getArrayCell()[i][j].getValue());
            }
        }

        // Copy the other properties of this board to the new board
        copy.availableMove = this.availableMove;
        copy.current_Player = this.current_Player;

        return copy;
    }

    public void markPos(int row, int col, String Player) {
        this.arrayCell[row][col].setValue(Player);
        this.availableMove--;
    }

    public void Undo(int row, int col) {
        this.arrayCell[row][col].setValue(Cell.Empty_value);
        this.availableMove++;
    }

    public Board(int boardSize, int sizeJframe) {

        this.boardSize = boardSize;     //kích thươc bàn cờ
        this.sizeJframe = sizeJframe;   //kích thước Jframe
        availableMove = boardSize * boardSize;      //số nước đi có thể đi
        size = (int) sizeJframe/boardSize;      //kích thước của cell
        current_Player = Cell.X_Value;          //khởi tại cho người chơi có kí hiệu là X
        try {
            imgX = ImageIO.read(getClass().getResource("X.png"));
            imgO = ImageIO.read(getClass().getResource("O.png"));
        }catch(Exception e){
            e.printStackTrace();
        }

        arrayCell = new Cell[boardSize][boardSize];     //tạo mảng Cell để lưu thông tin bàn cờ
        this.initMatrix();      //khởi tạo mảng Cell rỗng

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

    }

    private void initMatrix(){
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                arrayCell[i][j] =  new Cell();

                int x = i * size;
                int y = j * size;

                arrayCell[i][j].setX(y);
                arrayCell[i][j].setY(x);
                arrayCell[i][j].setWidth(size-1);
                arrayCell[i][j].setHeight(size-1);
                arrayCell[i][j].setValue("");
            }
        }
    }

    private String CheckWin(int col, int row, String value){
        if(availableMove <= 0) {
            return "Hòa";
        }
        for (int i = 0; i < 3; i++) {
            if(arrayCell[i][0].getValue() == arrayCell[i][1].getValue() && arrayCell[i][0].getValue() == arrayCell[i][2].getValue() && arrayCell[i][0].getValue() != "") {
                if(arrayCell[i][0].getValue().equals(Cell.X_Value)) {
                    return "Bạn thắng";
                } else {
                    return "AI thắng";
                }
            }
            if(arrayCell[0][i].getValue() == arrayCell[1][i].getValue() && arrayCell[0][i].getValue() == arrayCell[2][i].getValue() && arrayCell[0][i].getValue() != "") {
                if(arrayCell[0][i].getValue().equals(Cell.X_Value)) {
                    return "Bạn thắng";
                } else {
                    return "AI thắng";
                }
            }
        }
        if (arrayCell[0][0].getValue() == arrayCell[1][1].getValue() && arrayCell[0][0].getValue() == arrayCell[2][2].getValue() && arrayCell[0][0].getValue() != "") {
            if(arrayCell[0][0].getValue().equals(Cell.X_Value)) {
                return "Bạn thắng";
            } else {
                return "AI thắng";
            }
        }
        if (arrayCell[0][2].getValue() == arrayCell[1][1].getValue() && arrayCell[0][2].getValue() ==arrayCell [2][0].getValue() && arrayCell[0][2].getValue() != "") {
            if(arrayCell[0][2].getValue().equals(Cell.X_Value)) {
                return "Bạn thắng";
            } else {
                return "AI thắng";
            }
        }
        return null;
    }

    private void EndGame(int col, int row, String value) {
        String result = CheckWin(col,row,value);
        if (result != null) {
            int reply = JOptionPane.showConfirmDialog(null, "Trò chơi kết thúc!\n" + result + "\nBạn có muốn chơi lại?", "Tiếp tục?",  JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION)
            {
                availableMove = boardSize * boardSize;
                initMatrix();
                repaint();
                setCurrent_Player(Cell.X_Value);
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
//            int [] coordinates = Minimax.getBestMove(board_copy);
            int [] coordinates = MonteCarlo.findBestMove(board_copy);
            x = coordinates[0];
            y = coordinates[1];
            System.out.println("AI: "+ x + " " + y);
            markPos(x,y, Cell.O_Value);
            EndGame(x,y,arrayCell[x][y].getValue());
            repaint();
            setCurrent_Player(Cell.X_Value);
        }
    }

    private void getHumanMove (int xM, int yM) {
        //tính toán nhân vào cell nào
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Cell cell = arrayCell[i][j];
                int cX_Start = cell.getX();
                int cY_Start = cell.getY();
                int cX_End = cell.getX() + cell.getWidth();
                int cY_End = cell.getY() + cell.getHeight();

                if (cX_Start <= xM && cX_End >= xM && cY_Start <= yM && cY_End >= yM) {
                    System.out.println("Đã chọn ô ở hàng " + (i+1) + " cột " + (j+1));

                    if(arrayCell[i][j].getValue() == "") {
                        markPos(i,j,Cell.X_Value);
                        repaint();
                        EndGame(i,j,arrayCell[i][j].getValue());
                        setCurrent_Player(Cell.O_Value);
                        PlayAI();
//                        printMatrix();
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
                    if (arrayCell[i][j].getValue() != " "){
                        System.out.println(arrayCell[i][j].getValue());
                    } else {
                        System.out.println(" ");
                    }
                } else {
                    System.out.print(arrayCell[i][j].getValue() + " | ");
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

                graphic2d.fillRect(arrayCell[i][j].getX(),arrayCell[i][j].getY(),arrayCell[i][j].getWidth() - 1,arrayCell[i][j].getHeight() - 1);

                if (arrayCell[i][j].getValue().equals(Cell.X_Value)) {
                    img = imgX;
                    graphic2d.drawImage(img, arrayCell[i][j].getX(),arrayCell[i][j].getY(),arrayCell[i][j].getWidth(),arrayCell[i][j].getHeight(), this);
                } else if(arrayCell[i][j].getValue().equals(Cell.O_Value)) {
                    img = imgO;
                    graphic2d.drawImage(img, arrayCell[i][j].getX(),arrayCell[i][j].getY(),arrayCell[i][j].getWidth() - 1,arrayCell[i][j].getHeight() - 1, this);
                }
                else {
                    img = null;
                    graphic2d.drawImage(img, arrayCell[i][j].getX(),arrayCell[i][j].getY(),arrayCell[i][j].getWidth() - 1,arrayCell[i][j].getHeight() - 1, this);
                }
            }
        }
    }
}