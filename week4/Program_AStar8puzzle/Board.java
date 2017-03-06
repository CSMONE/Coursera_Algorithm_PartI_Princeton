
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;

public class Board {
    
    private int[][] blockArr;
    private int n;
    private int hamScore;
    private int manScore;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        blockArr = new int[n][n];
        hamScore = 0;
        manScore = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0) {
                    blockArr[i][j] = blocks[i][j];
                    if (blocks[i][j] != i*n+j+1) {
                        hamScore++;
                        manScore = manScore + Math.abs((blocks[i][j] - 1) / n - i) 
                        + Math.abs((blocks[i][j] - 1) % n - j);
                    }
                }               
            }          
        }
    }
    
    
    // board dimension n
    public int dimension() {
        return n;
    }
       
    // number of blocks out of place
    public int hamming() {
        return hamScore;
    }
     
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manScore;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        return hamScore == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] blockArrCopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blockArrCopy[i][j] = blockArr[i][j];
            }
        }
        if (blockArrCopy[0][0] != 0 && blockArrCopy[0][1] != 0) {
            int temp = blockArrCopy[0][0];
            blockArrCopy[0][0] = blockArrCopy[0][1];
            blockArrCopy[0][1] = temp;
        }
        else {
            int temp = blockArrCopy[1][0];
            blockArrCopy[1][0] = blockArrCopy[1][1];
            blockArrCopy[1][1] = temp;
        }
        return new Board(blockArrCopy);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board thatBoard = (Board)y;  
        if (this.n != thatBoard.n) {  
            return false;  
        }
        int[][] yBoardArr = thatBoard.blockArr;  
        for (int i = 0; i < n; i++) {  
            for (int j = 0; j < n; j++) {  
                if (blockArr[i][j] != yBoardArr[i][j]) {  
                    return false;  
                }  
            }  
        }
        return true;       
    }
    
    private int[][] arrCopy() {  
        int[][] result = new int[n][n];  
        for (int i = 0; i < n; i++) {  
            for (int j = 0; j < n; j++) {  
                result[i][j] = blockArr[i][j];  
            }
        }  
        return result;  
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int blanki = n;  
        int blankj = n;  
        for (int i = 0; i < n; i++) {  
            for (int j = 0; j < n; j++) {  
                if (blockArr[i][j] == 0) { 
                    blanki = i;  
                    blankj = j;  
                }  
            }  
        }  
        MinPQ<Board> q = new MinPQ<Board>(new Comparator<Board>() {  
            public int compare(Board board1, Board board2) {
                if (board1.manhattan() < board2.manhattan()) return -1;  
                else if (board1.manhattan() == board2.manhattan()) return 0;  
                else return 1;  
            }  
        });
        if (blankj - 1 >= 0) {  
            int[][] temp = arrCopy();  
            temp[blanki][blankj] = temp[blanki][blankj - 1];  
            temp[blanki][blankj - 1] = 0;  
            q.insert(new Board(temp));
        }  
        if (blankj + 1 < n) {  
            int[][] temp = arrCopy();  
            temp[blanki][blankj] = temp[blanki][blankj + 1];  
            temp[blanki][blankj + 1] = 0;  
            q.insert(new Board(temp)); 
        }  
        if (blanki - 1 >= 0) {  
            int[][] temp = arrCopy();  
            temp[blanki][blankj] = temp[blanki - 1][blankj];  
            temp[blanki - 1][blankj] = 0;  
            q.insert(new Board(temp)); 
        }  
        if (blanki + 1 < n) {  
            int[][] temp = arrCopy();  
            temp[blanki][blankj] = temp[blanki + 1][blankj];  
            temp[blanki + 1][blankj] = 0;  
            q.insert(new Board(temp)); 
        }  
        return q;         
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blockArr[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}