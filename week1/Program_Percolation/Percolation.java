
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF backWashGrid;
    private boolean[] openedArr;
    private int n;

    public Percolation(int m) {
        if (m <= 0) {
            throw new IllegalArgumentException("Argument must be positive");
        }
        n = m;
        grid = new WeightedQuickUnionUF(n*n+2);
        backWashGrid = new WeightedQuickUnionUF(n*n+1);
        openedArr = new boolean[n*n+2]; 
        for (int i = 0; i < n*n+2; i++) {
            openedArr[i] = false; 
        }
    }  

    public void open(int row, int col) {
        if (row < 1 || row > n) {
            throw new IndexOutOfBoundsException("row index " + row + " out of bounds");
        }  
        if (col < 1 || col > n) {  
            throw new IndexOutOfBoundsException("row index " + col + " out of bounds");  
        }  
        int pos = (row-1)*n+col;
        if (!openedArr[pos]) {
            openedArr[pos] = true;

            if (row > 1 && isOpen(row-1, col)) {
                grid.union(pos, pos-n);
                backWashGrid.union(pos, pos-n);
            }

            if (row == 1) {
                grid.union(pos, 0);
                backWashGrid.union(pos, 0);
            } 
              
            if (row < n && isOpen(row+1, col)) {
                grid.union(pos, pos+n); 
                backWashGrid.union(pos, pos+n);
            }

            if (row == n) {
                grid.union(pos, n*n+1);
            } 
      
            if (col > 1 && isOpen(row, col-1)) {
                grid.union(pos, pos-1);
                backWashGrid.union(pos, pos-1);
            }
      
            if (col < n && isOpen(row, col+1)) {
                grid.union(pos, pos+1); 
                backWashGrid.union(pos, pos+1);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n) {  
            throw new IndexOutOfBoundsException("row index " + row + " out of bounds");  
        }  
        if (col < 1 || col > n) {  
            throw new IndexOutOfBoundsException("row index " + col + " out of bounds");  
        }
        return openedArr[(row-1)*n+col];
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > n) {  
            throw new IndexOutOfBoundsException("row index " + row + " out of bounds");  
        }  
        if (col < 1 || col > n) {  
            throw new IndexOutOfBoundsException("row index " + col + " out of bounds");  
        }
        int pos = (row-1)*n+col;
        return backWashGrid.connected(0, pos);
    }  

    public int numberOfOpenSites() {
        int num = 0;
        for (int i = 1; i < n*n+1; i++) {
            if (openedArr[i]) {
                num++;
            }
        }
        return num;
    }

    public boolean percolates() {
        return grid.connected(0, n*n+1);
    }

}




