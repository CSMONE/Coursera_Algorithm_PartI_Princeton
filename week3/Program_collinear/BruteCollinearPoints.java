
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {

    private LineSegment[] lines;
    private int lineNumber;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.NullPointerException("argument is null"); 
        }
        lineNumber = 0;
        int pointNumber = points.length;  
        Stack<LineSegment> lineStack = new Stack<LineSegment>();
        int[] index = new int[4];    
        Point[] pointsCopy = new Point[pointNumber];  
        System.arraycopy(points, 0, pointsCopy, 0, pointNumber);
        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointNumber; i++) {
            if (pointsCopy[i] == null) {
                throw new java.lang.NullPointerException("a point is null"); 
            }
            if (i < pointNumber-1 && pointsCopy[i].compareTo(pointsCopy[i+1]) == 0) {
                throw new java.lang.IllegalArgumentException("contains a repeated point");
            }
        }
        for (index[0] = 0; index[0] < pointNumber - 3; index[0]++) {
            for (index[1] = index[0] + 1; index[1] < pointNumber - 2; index[1]++) {
                for (index[2] = index[1] + 1; index[2] < pointNumber - 1; index[2]++) {
                    for (index[3] = index[2] + 1; index[3] < pointNumber; index[3]++) {
                        double s1 = pointsCopy[index[0]].slopeTo(pointsCopy[index[1]]);
                        double s2 = pointsCopy[index[1]].slopeTo(pointsCopy[index[2]]);
                        double s3 = pointsCopy[index[2]].slopeTo(pointsCopy[index[3]]);
                        if (((s1 == s2) && (s2 == s3))) {
                            Point max = pointsCopy[index[0]];
                            Point min = pointsCopy[index[0]];
                            for (int i = 1; i < 4; i++) {
                                if (pointsCopy[index[i]].compareTo(max) == 1) {
                                    max = pointsCopy[index[i]];
                                }
                                if (pointsCopy[index[i]].compareTo(min) == -1) {
                                    min = points[index[i]];
                                }
                            }
                            LineSegment line = new LineSegment(min, max);
                            lineStack.push(line);
                            lineNumber++;
                        }
                    }
                }
            }
        }
        lines = new LineSegment[lineNumber];
        for (int i = 0; i < lineNumber; i++) {
            lines[i] = lineStack.pop();
        }
    }    

    // the number of line segments
    public int numberOfSegments() {
        return lineNumber;
    }       

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(lines, lineNumber);
    }               


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}