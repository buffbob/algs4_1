/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size = 0;
    private Point2D closest;

    private class Node {
        private final Point2D point;
        private Node previous;
        private Node left;
        private Node right;
        private RectHV rect;
        private boolean isVertical = true;

        public Node(Point2D p, boolean isVertical, Node parent) {
            this.point = p;
            this.isVertical = isVertical;

            if (parent == null) { // the root?
                this.rect = new RectHV(0, 0, 1, 1);
            }
            else { // find new dimensions of this.rect
                double minX = parent.rect.xmin();
                double minY = parent.rect.ymin();
                double maxX = parent.rect.xmax();
                double maxY = parent.rect.ymax();

                int result = parent.compareTo(p);
                // + result means that parent is > p
                if (isVertical) {
                    if (result > 0) {
                        maxY = parent.point.y();
                    }
                    else {
                        minY = parent.point.y();
                    }
                }
                else {
                    if (result > 0) { // p is smaller than parent
                        maxX = parent.point.x();
                    }
                    else {
                        minX = parent.point.x();
                    }
                }
                this.rect = new RectHV(minX, minY, maxX, maxY);
            }
        }

        private int compareTo(Point2D that) {
            if (isVertical) return Double.compare(this.point.x(), that.x());
            else return Double.compare(this.point.y(), that.y());
        }

        public void draw() {
            StdDraw.setPenRadius(0.005);

            if (isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
            }
            StdDraw.setPenRadius(0.5);
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();
            if (left != null) left.draw();
            if (right != null) right.draw();
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("insert has null Point2D");

        root = insertNode(root, p, null, true);
    }

    private Node insertNode(Node current, Point2D p, Node previous, boolean isVert) {
        if (current == null) { // the case when we have found where to insert the node
            size++;
            return new Node(p, isVert, previous);
        }
        if (current.point.compareTo(p) == 0) return current; // duplicates

        if (current.compareTo(p) > 0) { // current.point is greater than p-- go left
            current.left = insertNode(current.left, p, current, !isVert);
        } else {
            current.right = insertNode(current.right, p, current, !isVert);
        }
        return current;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("contains() has a null argument");
        return get(root, p) != null;
    }

    private Node get(Node n, Point2D p) {
        if (n == null) return null; // nothing found
        if (n.point.equals(p)) return n; // it is found!!!
        // check compare and recurse
        if (n.compareTo(p) > 0) { // n.point is larger so go left
            return get(n.left, p);
        } else {
            return get(n.right, p);
        }
    }

    public void draw() {
        if (root != null) {
            root.draw();
        }
    }

    public Iterable<Point2D> range(RectHV queryRect) {
        if (queryRect == null) throw new IllegalArgumentException("range() has a null argument");
        // List<Point2D> thePts = new ArrayList<>();
        return range(root, queryRect, new ArrayList<Point2D>());
    }

    private List<Point2D> range(Node node, RectHV query, List<Point2D> inRange) {
        if (node == null) return inRange;
        if (node.rect.intersects(query)) { // if true, there might be a point in this direction, continue search
            if (query.contains(node.point)) inRange.add(node.point);
            range(node.left, query, inRange);
            range(node.right, query, inRange);
        }
        return inRange;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("nearest() has null argument");
        if (isEmpty()) return null;
        closest = root.point;
        nearestPoint(root, p);
        return closest;
    }

    private void nearestPoint(Node node, Point2D query) {
        if (node == null) return;
        // the test
        if (node.rect.distanceSquaredTo(query) < query.distanceSquaredTo(closest)) { // some parts of the rect could hold closer points so keep going
            // check whether to update closes
            updateClosestMaybe(node.point, query);

            if (node.compareTo(query) > 0) { // query is smaller so go in that direction first to prune potential search area.
                nearestPoint(node.left, query);
                nearestPoint(node.right, query);
            } else {
                nearestPoint(node.right, query);
                nearestPoint(node.left, query);
            }
        }
    }
    
    private boolean updateClosestMaybe(Point2D nodePt, Point2D queryPt) {
        if (closest == null) {
            closest = nodePt;
            return true;
        }
        double distanceToClosest = closest.distanceSquaredTo(queryPt);
        double distanceToNodePt = nodePt.distanceSquaredTo(queryPt);

        if (distanceToClosest > distanceToNodePt) { // needs update
            closest = nodePt;
            return true;
        }
        return false;
    }
    
    public static void main(String[] args) {
        KdTree k = new KdTree();
        k.insert(new Point2D(1.0, 0.0));
        k.insert(new Point2D(1.0, 0.0));
        System.out.println(k.size());


    }
}
