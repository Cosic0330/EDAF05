import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Math;

public class closest {
    private static int nbrPoints; // Antalet punkter
    private static ArrayList<Point> pointsX; //Sorterade efter X värde

    static class Point implements Comparable<Point>, Comparator<Point> {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;

        }

        public Point() {
        }

        @Override
        public int compareTo(Point p) {
            if (x - p.x > 0) {
                return 1;
            } else if (x - p.x < 0) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public int compare(Point p1, Point p2) {
            if (p1.y - p2.y > 0) {
                return 1;
            } else if (p1.y - p2.y < 0) {
                return -1;
            } else {
                return 0;
            }
        }

        public double distanceTo(Point point) {
            double ac = Math.abs(point.y - y);
            double cb = Math.abs(point.x - x);

            return Math.hypot(ac, cb);
        }

        public String toString() {
            return ("koord: " + x + ", " + y + ") ");
        }
    }

    public void handleData() {
        pointsX = new ArrayList<>();
        Scanner scan = null;
        scan = new Scanner(System.in);
        nbrPoints = scan.nextInt();

        for (int i = 0; i < nbrPoints; i++) {
            int xs = scan.nextInt();
            int ys = scan.nextInt();
            Point p = new Point(xs, ys);
            pointsX.add(p);
        }

        Collections.sort(pointsX);

        scan.close();
    }

    public double findClosest(ArrayList<Point> pointList) {
        double dist = Double.MAX_VALUE;
        if (pointList.size() <= 3) {
            for (int i = 0; i < pointList.size(); i++) {
                for (int j = i; j < pointList.size(); j++) {    
                    if (i != j) {
                        double temp = pointList.get(i).distanceTo(pointList.get(j));
                        if (temp < dist) {
                            dist = temp;
                        }
                    }
                }
            }
            return dist;
        }

        ArrayList<Point> xLeft = new ArrayList<Point>(pointList.subList(0, pointList.size() / 2));
        ArrayList<Point> xRight = new ArrayList<Point>(pointList.subList(pointList.size() / 2, pointList.size()));
        double distLeft = findClosest(xLeft);
        double distRight = findClosest(xRight);
        double middle = pointList.get(pointList.size() / 2).x;
        double delta = Math.min(distLeft, distRight);

        ArrayList<Point> yPoints = new ArrayList<>();
        for (Point p : pointList) {
            if (Math.abs(p.x - middle) < delta) {
                yPoints.add(p);
            }
        }
        Collections.sort(yPoints, new Point());
        for (int i = 0; i < yPoints.size(); i++) {
            for (int j = i + 1; j < i+15; j++) {
                if(j<yPoints.size()){
                double temp = yPoints.get(i).distanceTo(yPoints.get(j));
                if (temp < delta) {
                    delta = temp;
                }
            }
            }
        }
        return delta;

    }

    public static void main(String[] args) {
        closest cl = new closest();
        cl.handleData();
        System.out.println(String.format(java.util.Locale.US, "%.6f", cl.findClosest(pointsX)));
    }

}

//time complexity O(nlogn) if n is the number of points. The dividing into two halves takes O(logn) and the merge takes O(n). 
// Sufficient to check a few points along to mid line since there can only be one dot in each "square" and therefore the distance to 
// points further away would be pointless since the distance is bigger then delta. 
// break recursion when we have 3 or less points in the list of points. 