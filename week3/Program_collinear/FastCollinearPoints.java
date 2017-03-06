import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {

    private LineSegment[] lines;
    private int lineNumber;
    private Point[] startPoints;
    private Point[] endPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.NullPointerException("argument is null"); 
        }
        lineNumber = 0;
        int pointNumber = points.length;  
        // Stack<LineSegment> lineStack = new Stack<LineSegment>();
        Stack<Point> startPointStack = new Stack<Point>();
        Stack<Point> endPointStack = new Stack<Point>();
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
        for (int p0 = 0; p0 < pointNumber - 3; p0++) {
            Point[] tmpPoints = new Point[pointNumber - p0 - 1];
            for (int p1 = p0 + 1; p1 < pointNumber; p1++) {              
                tmpPoints[p1 - p0 - 1] = pointsCopy[p1];             
            }

            Arrays.sort(tmpPoints, pointsCopy[p0].slopeOrder());
            int index1 = 0;
            while (index1 < pointNumber - p0 - 3) {
                int index2 = index1 + 2;
                Point point0 = pointsCopy[p0];
                double slope1 = point0.slopeTo(tmpPoints[index1]);
                if (slope1 == point0.slopeTo(tmpPoints[index2])) {
                    while (index2 < pointNumber - p0 - 1 && slope1 == point0.slopeTo(tmpPoints[index2])) {
                        index2++;                           
                    }

                    Point max = point0;
                    Point min = point0;
                    for (int i = index1; i < index2; i++) {
                        if (tmpPoints[i].compareTo(max) == 1) {
                            max = tmpPoints[i];
                        }
                        if (tmpPoints[i].compareTo(min) == -1) {
                            min = tmpPoints[i];
                        }
                    }
                    // LineSegment line = new LineSegment(min, max);
                    // lineStack.push(line);
                    startPointStack.push(min);
                    endPointStack.push(max);
                    lineNumber++;
                    index1 = index2;
                }
                else {
                    index1++;
                }
            }
        }
        // lines = new LineSegment[lineNumber];
        startPoints = new Point[lineNumber];
        endPoints = new Point[lineNumber];
        
        for (int i = 0; i < lineNumber; i++) {
            // lines[i] = lineStack.pop();
            startPoints[i] = startPointStack.pop();
            endPoints[i] = endPointStack.pop();
            
        }
        // StdOut.println(startPoints.length);
        Stack<LineSegment> newPonitsStack = new Stack<LineSegment>(); 
        int newlineNumber = 0;
        for (int i = 0; i < lineNumber; i++) {
            
            if (startPoints[i] != null) {
                // StdOut.println("istart");
                // LineSegment linei = new LineSegment(startPoints[i], endPoints[i]);
                // StdOut.println(linei);
                // StdOut.println("iend");
                for (int j = i + 1; j < lineNumber; j++) {
                
                    if (startPoints[j] != null) {
                        // StdOut.println("jstart");
                        double s1 = startPoints[i].slopeTo(endPoints[i]);
                        double s2 = startPoints[i].slopeTo(startPoints[j]);
                        double s3 = startPoints[i].slopeTo(endPoints[j]);
                        // StdOut.println(s1+","+s2+","+s3);
                        if ((startPoints[i] == startPoints[j] && s1 == s3) 
                            || (endPoints[i] == endPoints[j] && s1 == s2) 
                            || (s1 == s2 && s2 == s3)) {
                            // LineSegment linej = new LineSegment(startPoints[j], endPoints[j]);
                            // StdOut.println(linej);
                            if (startPoints[i].compareTo(startPoints[j]) == 1) {
                                startPoints[i] = startPoints[j];
                            }
                            if (endPoints[i].compareTo(endPoints[j]) == -1) {
                                endPoints[i] = endPoints[j];
                            }
                            startPoints[j] = null;
                            endPoints[j] = null;
                        }
                    }
                }
                LineSegment line = new LineSegment(startPoints[i], endPoints[i]);
                newPonitsStack.push(line);
                newlineNumber++;
            }
     
        }
        // StdOut.println("end");
        lineNumber = newlineNumber;
        lines = new LineSegment[lineNumber];
        for (int i = 0; i < lineNumber; i++) {
            lines[i] = newPonitsStack.pop();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}