package graphics.primitives.impl;

import graphics.primitives.*;
import graphics.HandlePoint;
import graphics.RotationPoint;
import java.awt.Color;


public class Arc extends AbstractGraphicPrimitive {

    private double radius;
    private double startAngle;
    private double endAngle;

    public Arc() {
        super();
    }

    public Arc(Color color, int x, int y) {
        super();
        super.setBorderColor(color);
        this.addPoint(x, y);
    }

    public Arc(Arc arc) {
        super();
        for (HandlePoint hp : arc.getHandlePoints()) {
            handlePoints.add( new HandlePoint(hp) );
        }
        this.setSelected(arc.isSelected());
        this.setBorderColor(arc.getBorderColor());
        this.setRotationPoint(new RotationPoint(arc.getRotationPoint()));
    }

    public void addPoint(int x, int y) {
        handlePoints.add(new HandlePoint(this, x, y));
    }

    public boolean isPointFromLine(int x, int y) {
        return false;
    }

    public void removeLastPoint() {
        handlePoints.remove(handlePoints.get(handlePoints.size() - 1));
    }

//    /**
//     * @return the angle
//     */
//    public double getAngle() {
//        HandlePoint hp1 = new HandlePoint(handlePoints.get(1));
//        HandlePoint hp2 = new HandlePoint(handlePoints.get(2));
//
//        hp1.substractHandlePoint(handlePoints.get(0));
//        hp2.substractHandlePoint(handlePoints.get(0));
//
//        return Math.toDegrees(Calcs.getAngle(hp1, hp2));
//    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
        //return  Calcs.getDistance(handlePoints.get(0), handlePoints.get(2));
    }

//    public double getAngleOffset() {
//        HandlePoint hp = new HandlePoint(handlePoints.get(2));
//        hp.substractHandlePoint(handlePoints.get(0));
//
//        HandlePoint newPoint = new HandlePoint(this, hp.getX(), 0);
//
//        return Math.toDegrees(Calcs.getAngle(newPoint, hp));
//    }

    public HandlePoint getCenter() {
        return handlePoints.get(0);
    }

    /**
     * @return the startAngle
     */
    public double getStartAngle() {
        return startAngle;
    }

    /**
     * @param startAngle the startAngle to set
     */
    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    /**
     * @return the endAngle
     */
    public double getEndAngle() {
        return endAngle;
    }

    /**
     * @param endAngle the endAngle to set
     */
    public void setEndAngle(double endAngle) {
        this.endAngle = endAngle;
    }
}
