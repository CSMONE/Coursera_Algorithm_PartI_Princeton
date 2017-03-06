// import java.util.Iterator;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node rootNode;
    private int size;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
     }
    
    // construct an empty set of points 
    public KdTree() {
        size = 0;     
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }
    
    // number of points in the set 
    public int size() {
        return size;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (size == 0) {
            rootNode = new Node();
            rootNode.p = p;
            rootNode.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            size++; 
        }
        recinsert(rootNode, p, true);
    }
    
    private void recinsert(Node node, Point2D point, boolean isVertical) {
        if (node.p.equals(point)) {
            return;
        }
        Node newNode = new Node();
        newNode.p = point;
        RectHV nodeRect = node.rect;
        if (isVertical) {
            if (node.p.x() > point.x()) {
                if (node.lb == null) {
                    newNode.rect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.p.x(), nodeRect.ymax());
                    node.lb = newNode;
                    size++;
                }
                else {
                    recinsert(node.lb, point, !isVertical);
                }
            }
            else {
                if (node.rt == null) {
                    newNode.rect = new RectHV(node.p.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
                    node.rt = newNode;
                    size++;
                }
                else {
                    recinsert(node.rt, point, !isVertical);
                }
            }
        }
        else {
            if (node.p.y() > point.y()) {
                if (node.lb == null) {
                    newNode.rect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.p.y());
                    node.lb = newNode;
                    size++;
                }
                else {
                    recinsert(node.lb, point, isVertical);
                }
            }
            else {
                if (node.rt == null) {
                    newNode.rect = new RectHV(nodeRect.xmin(), node.p.y(), nodeRect.xmax(), nodeRect.ymax());
                    node.rt = newNode;
                    size++;
                }
                else {
                    recinsert(node.rt, point, isVertical);
                }
            }
        }
        
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (rootNode == null) {
            return false;
        }
        return reccontain(rootNode, p, true);
    }
    
    private boolean reccontain(Node node, Point2D point, boolean isVertical) {
        if (node.p.equals(point)) {
            return true;
        }
        if (isVertical) {
            if (node.p.x() > point.x()) {
                if (node.lb == null) {
                    return false;
                }
                else {
                    return reccontain(node.lb, point, !isVertical);
                }
            }
            else {
                if (node.rt == null) {
                    return false;
                }
                else {
                    return reccontain(node.rt, point, !isVertical);
                }               
            }
        }
        else {
            if (node.p.y() > point.y()) {
                if (node.lb == null) {
                    return false;
                }
                else {
                    return reccontain(node.lb, point, isVertical);
                }
            }
            else {
                if (node.rt == null) {
                    return false;
                }
                else {
                    return reccontain(node.rt, point, isVertical);
                }
                
            }
        }
        
    }
    
    // draw all points to standard draw 
    public void draw() {
        draw(rootNode, true);
    }

    // helper for draw
    private void draw(Node n, boolean isVertical) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(n.p.x(), n.p.y());
        StdDraw.setPenRadius();
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        draw(n.lb, !isVertical);
        draw(n.rt, !isVertical);
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> pointsQueue = new Queue<Point2D>();
        recrange(rootNode, rect, pointsQueue);
        return pointsQueue;
        
    }
    
    private void recrange(Node n, RectHV r, Queue<Point2D> q) {
        if (n == null || !n.rect.intersects(r)) return;
        if (r.contains(n.p)) {
            q.enqueue(n.p);
        }
        recrange(n.lb, r, q);
        recrange(n.rt, r, q);
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (size == 0) {
            return null;
        }
        return recnearest(rootNode, p, rootNode.p, true);
    }
    
    private Point2D recnearest(Node n, Point2D p, Point2D currentNear, boolean isVertical) {
        double currentMinDist = currentNear.distanceSquaredTo(p);
        if (n == null || currentMinDist <= n.rect.distanceSquaredTo(p)) return currentNear;
        if (currentMinDist > n.p.distanceSquaredTo(p)) currentNear = n.p;
        double cmp = isVertical ? p.x() - n.p.x() : p.y() - n.p.y();
        if (cmp < 0) {
            Point2D lbNear = recnearest(n.lb, p, currentNear, !isVertical);
            if (n.rt == null || lbNear.distanceSquaredTo(p) <= n.rt.rect.distanceSquaredTo(p)) {
                return lbNear;
            } else {
                Point2D rtNear = recnearest(n.rt, p, currentNear, !isVertical);
                return lbNear.distanceSquaredTo(p) <= rtNear.distanceSquaredTo(p) ? lbNear : rtNear;
            }
        } else {
            Point2D rtNear = recnearest(n.rt, p, currentNear, !isVertical);
            if (n.lb == null || rtNear.distanceSquaredTo(p) <= n.lb.rect.distanceSquaredTo(p)) {
                return rtNear;
            } else {
                Point2D lbNear = recnearest(n.lb, p, currentNear, !isVertical);
                return lbNear.distanceSquaredTo(p) <= rtNear.distanceSquaredTo(p) ? lbNear : rtNear;
            }
        }
    }


}
