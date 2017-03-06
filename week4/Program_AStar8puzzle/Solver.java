import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
 
    private int stepNumber;
    private boolean solvable;
    private SearchNode lastNode;
    
    private class SearchNode implements Comparable<SearchNode> {
        private Board searchBoard;
        private int moveNumber;
        private SearchNode previousNode; 
        private int priorityValue;
        
        public SearchNode(Board now, int move, SearchNode pre) {
            searchBoard = now;
            moveNumber = move;
            previousNode = pre;
            priorityValue = searchBoard.manhattan() + moveNumber;
        }
        
        public int compareTo(SearchNode that) {
            if (this.priorityValue > that.priorityValue) {
                return 1;
            }
            if (this.priorityValue < that.priorityValue) {
                return -1;
            }
            return 0;
        }      
    }
    
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.NullPointerException("argument is null");
        }
        Board initialBoard = initial;
        stepNumber = -1;
        
        SearchNode initialNode = new SearchNode(initialBoard, 0, null);
        SearchNode initialNodeTwin = new SearchNode(initialBoard.twin(), 0, null);
        MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>();
        priorityQueue.insert(initialNode);
        MinPQ<SearchNode> priorityQueueTwin = new MinPQ<SearchNode>();
        priorityQueueTwin.insert(initialNodeTwin);
        while (true) {
            SearchNode delNode = priorityQueue.delMin();
            if (delNode.searchBoard.isGoal()) {
                solvable = true;
                lastNode = delNode;
                stepNumber = delNode.moveNumber;
                break;
            }
            for (Board nbBoard: delNode.searchBoard.neighbors()) {
                if (delNode.previousNode == null || !nbBoard.equals(delNode.previousNode.searchBoard)) {
                    priorityQueue.insert(new SearchNode(nbBoard, delNode.moveNumber+1, delNode));
                }
            }
            
            SearchNode delNodeTwin = priorityQueueTwin.delMin();
            if (delNodeTwin.searchBoard.isGoal()) {
                solvable = false;
                lastNode = delNodeTwin;
                stepNumber = -1;
                break;
            }
            for (Board nbBoardTwin: delNodeTwin.searchBoard.neighbors()) {
                if (delNodeTwin.previousNode == null || !nbBoardTwin.equals(delNodeTwin.previousNode.searchBoard)) {
                    priorityQueueTwin.insert(new SearchNode(nbBoardTwin, delNodeTwin.moveNumber+1, delNodeTwin));
                }
            }
        }
        
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return stepNumber;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        else {
            Stack<Board> resultPath = new Stack<Board>();
            SearchNode tmp = lastNode;
            while (tmp != null) {
                resultPath.push(tmp.searchBoard);
                tmp = tmp.previousNode;
                
            }
            
            return resultPath;
        }
        
    }
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
     // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
