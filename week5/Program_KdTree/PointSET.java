import java.util.Iterator;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
   
    private SET<Point2D> setTree = new SET<Point2D>();
    
    // construct an empty set of points 
    public PointSET() {
        setTree = new SET<Point2D>();;
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return setTree.isEmpty();
    }
    
    // number of points in the set 
    public int size() {
        return setTree.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!contains(p)) {
            setTree.add(p);
        }
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        return setTree.contains(p);
    }
    
    // draw all points to standard draw 
    public void draw() {
        Iterator<Point2D> points = setTree.iterator();
        while (points.hasNext()) {
            points.next().draw();
        }
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> pointQueue = new Queue<Point2D>();
        Iterator<Point2D> pointIter = setTree.iterator();
        while (pointIter.hasNext()) {
            Point2D p = pointIter.next();
            if (rect.contains(p)) {
                pointQueue.enqueue(p);
            }
        }
        return pointQueue;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (setTree.isEmpty()) {
            return null;
        }
        Iterator<Point2D> pointIter = setTree.iterator();
        Point2D nearestPoint = pointIter.next();
        double nearestDistance = p.distanceTo(nearestPoint);
        while (pointIter.hasNext()) {
            Point2D nextPoint = pointIter.next();
            double newDistance = p.distanceTo(nearestPoint);
            if (newDistance < nearestDistance) {
                nearestPoint = nextPoint;
                nearestDistance = newDistance;
            }
        }
        return nearestPoint;
    }

}
