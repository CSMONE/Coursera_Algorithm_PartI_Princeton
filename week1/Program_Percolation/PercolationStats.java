
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double meanVal;
    private double stdVal;
    private double[] fracArr;
    private int n;
    private int t;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int m, int trials)  {
        if (m <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Argument must be positive");
        }
        n = m;
        t = trials;
        fracArr = new double[trials];
    
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            int count = 0;
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n)+1;  
                int col = StdRandom.uniform(n)+1;  
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    count++;
                }
            }
            fracArr[i] = 1.0*count/(1.0*n*n);
        }
        meanVal = StdStats.mean(fracArr);  
        stdVal = StdStats.stddev(fracArr);

    }

    // sample mean of percolation threshold
    public double mean() {
        return meanVal;
    }                          

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdVal;
    }                        
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return meanVal - 1.96*stdVal/Math.sqrt(t);
    }                  
    
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return meanVal + 1.96*stdVal/Math.sqrt(t);
    }                  
    
    // test client (described below)
    public static void main(String[] args) {
        int m = StdIn.readInt();  
        int trials = StdIn.readInt();  
        PercolationStats percolationStats = new PercolationStats(m, trials);  
        StdOut.println("mean = " + percolationStats.mean());  
        StdOut.println("stddev = " + percolationStats.stddev());  
        StdOut.println("95% confidence interval " + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi());  
    
    }
}

