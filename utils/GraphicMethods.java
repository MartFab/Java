package utils;

import graphics.HandlePoint;
import graphics.primitives.impl.Curve;
import graphics.primitives.impl.Line;
import graphics.primitives.impl.PolyLine;

public class GraphicMethods {

    protected static int SELECTION_RADIUS = 4;

    public GraphicMethods() {
    }

    public static boolean linesIntersect(Line lineA, Line lineB) {
        return (ccw(lineA.getStartPoint(), lineA.getEndPoint(), lineB.getStartPoint()) *
                ccw(lineA.getStartPoint(), lineA.getEndPoint(), lineB.getEndPoint()) < 0) &&
                (ccw(lineB.getStartPoint(), lineB.getEndPoint(), lineA.getStartPoint()) *
                ccw(lineB.getStartPoint(), lineB.getEndPoint(), lineA.getEndPoint()) < 0);
    }

    public static int ccw(HandlePoint a, HandlePoint b, HandlePoint c) {
        double dx1 = b.getX() - a.getX();
        double dx2 = c.getX() - a.getX();
        double dy1 = b.getY() - a.getY();
        double dy2 = c.getY() - a.getY();
        int retvalue = 0;

        if (dx1 * dy2 > dy1 * dx2) {
            retvalue = -1;
        }
        if (dx1 * dy2 < dy1 * dx2) {
            retvalue = 1;
        }

        return retvalue;
    }

    public static boolean isPointInsideLine(int x, int y, int x0, int y0, int x1, int y1) {
        int stepx, stepy;

        int dy = y1 - y0;
        int dx = x1 - x0;


        if (dy < 0) {
            dy = -dy;
            stepy = -1;
        } else {
            stepy = 1;
        }
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else {
            stepx = 1;
        }
        dy <<= 1;                                                  // dy is now 2*dy
        dx <<= 1;                                                  // dx is now 2*dx

        if (pointInRange(x0, y0, x, y)) {
            return true;
        }

        if (dx > dy) {
            int fraction = dy - (dx >> 1);                       // same as 2*dy - dx
            while (x0 != x1) {
                //System.out.println("x0 = " + x0 + " x1 = " + x1 + " dx / 2 = " + (x0 + (dx / 2)) + " dx = " + dx);
                if (fraction >= 0) {
                    y0 += stepy;
                    fraction -= dx;                                // same as fraction -= 2*dx
                }
                x0 += stepx;
                fraction += dy;                                    // same as fraction -= 2*dy
                if (pointInRange(x0, y0, x, y)) {
                    return true;
                }
            }
        } else {
            int fraction = dx - (dy >> 1);
            while (y0 != y1) {
                if (fraction >= 0) {
                    x0 += stepx;
                    fraction -= dy;
                }
                y0 += stepy;
                fraction += dx;
                if (pointInRange(x0, y0, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPointInsideCurve(Curve curve, int x, int y) {
        double a;
        double b;

        HandlePoint beg;
        HandlePoint end;
        HandlePoint point = new HandlePoint(curve, 0, 0);
        int color[];

        switch (curve.getHandlePoints().size()) {

            case 0: break;
            case 1: break;
            case 2:
//                beg = getStartPoint();
//                end = getEndPoint();
//                Line line = new Line(beg, end);
//                line.setBorderColor(getBorderColor());
//                drawLine(line);
                break;
            case 3:
                // QUADRATIC BEZIER
                beg = curve.getStartPoint();
                end = curve.getEndPoint();
                HandlePoint cp = curve.getLastAddedHandlePoint();

                for (double t = 0; t <= 1; t += 0.0001) {
                    a = t;
                    b = 1 - t;

                    point.setX(beg.getX() * b * b +
                            2 * cp.getX() * b * a +
                            end.getX() * a * a);

                    point.setY(beg.getY() * b * b +
                            2 * cp.getY() * b * a +
                            end.getY() * a * a);

                    if (pointInRange((int)point.getX(), (int)point.getY(), x, y)) {
                        return true;
                    }
                }

                break;
            case 4:
                // CUBIC BEZIER
                beg = curve.getStartPoint();
                end = curve.getEndPoint();
                HandlePoint cp1 = curve.getHandlePoints().get(2);
                HandlePoint cp2 = curve.getHandlePoints().get(3);

                for (double t = 0; t <= 1; t += 0.0001) {
                    a = t;
                    b = 1 - t;

                    point.setX(beg.getX() * b * b * b +
                            3 * cp1.getX() * b * b * a +
                            3 * cp2.getX() * b * a * a +
                            end.getX() * a * a * a);

                    point.setY(beg.getY() * b * b * b +
                            3 * cp1.getY() * b * b * a +
                            3 * cp2.getY() * b * a * a +
                            end.getY() * a * a * a);
                    if (pointInRange((int)point.getX(), (int)point.getY(), x, y)) {
                        return true;
                    }
                }
                break;
            default:
        }
        return false;
    }

    public static boolean pointInRange(int x0, int y0, int x1, int y1) {
        if (Math.abs(x0 - x1) <= SELECTION_RADIUS &&
                Math.abs(y0 - y1) <= SELECTION_RADIUS) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean pointInPolygon(PolyLine p, int x, int y) {
        int i, j;
        boolean c = false;

        int testx = (int)x;
        int testy = (int)y;

        for (i = 0, j = p.getHandlePoints().size() - 1; i < p.getHandlePoints().size(); j = i++) {
            double vertyi = p.getHandlePoints().get(i).getY();
            double vertxi = p.getHandlePoints().get(i).getX();
            double vertyj = p.getHandlePoints().get(j).getY();
            double vertxj = p.getHandlePoints().get(j).getX();

            if (((vertyi > testy) != (vertyj > testy)) &&
                    (testx < (vertxj - vertxi) * (testy - vertyi) / (vertyj - vertyi) + vertxi)) {
                c = !c;
            }
        }

        return c;
    }
}
