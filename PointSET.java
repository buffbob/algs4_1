/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private Set<Point2D> set;
    private int size;

    public PointSET() {
        this.set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("insert has null argument");
        set.add(p);
        size++;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("contains has null argument");
        return set.contains(p);
    }

    public int size() {
        return size;
    }

    public void draw() {
        for (Point2D pp : set) {
            pp.draw();
        }
    }

    public Iterable<Point2D> range(RectHV r) {
        if (r == null) throw new IllegalArgumentException("range has null argument");
        List<Point2D> temp = new ArrayList<>();
        for (Point2D pp : set) {
            if (r.contains(pp)) temp.add(pp);
        }
        return temp;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("nearest has null argument");
        double longest = 2.0; // really about 1.41
        Point2D close = null;
        for (Point2D pp : set) {
            if (p.distanceTo(pp) < longest) {
                longest = p.distanceTo(pp);
                close = pp;
            }
        }
        return close;
    }

    public static void main(String[] args) {
        System.out.println("booger");
        In in = new In("input10.txt");
        PointSET ps = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D tempPt = new Point2D(x, y);
            ps.insert(tempPt);
        }

        // now some simple tests
        Point2D a = new Point2D(0.372, 0.497);
        Point2D aa = new Point2D(0.3732, 0.497);
        System.out.println(ps.contains(a)); // true
        System.out.println(ps.contains(aa)); // false
    }
}
