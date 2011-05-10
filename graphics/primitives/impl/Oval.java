package graphics.primitives.impl;

import graphics.primitives.*;
import graphics.*;
import java.awt.Color;

public class Oval extends AbstractGraphicPrimitive{

    static final long serialVersionUID = 1L;
    private HandlePoint startPoint;
    private HandlePoint controlPoint;

    public Oval(Color color, int x, int y, int xAxis, int yAxis) {
        super.setBorderColor(color);
        startPoint = new HandlePoint(this, x, y);
        controlPoint = new HandlePoint(this, xAxis, yAxis);
        rotationPoint = new RotationPoint(this, x, y);
        handlePoints.add(startPoint);
        handlePoints.add(controlPoint);
    }

    public Oval(Oval oval) {
        startPoint = new HandlePoint(oval.getStartPoint());
        controlPoint = new HandlePoint(oval.getControlPoint());
        rotationPoint = new RotationPoint(this, startPoint.getX(), startPoint.getY());
        handlePoints.add(startPoint);
        handlePoints.add(controlPoint);
        setSelected(oval.isSelected());
        setRotationPoint(new RotationPoint(oval.getRotationPoint()));
    }
    @Override
    public String toString() {
        return "Oval " + getStartPoint() + getControlPoint();
    }

    /**
     * @return the startPoint
     */
    public HandlePoint getStartPoint() {
        return startPoint;
    }

    /**
     * @param startPoint the startPoint to set
     */
    public void setStartPoint(HandlePoint startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * @return the controlPoint
     */
    public HandlePoint getControlPoint() {
        return controlPoint;
    }

    /**
     * @param controlPoint the controlPoint to set
     */
    public void setControlPoint(HandlePoint controlPoint) {
        this.controlPoint = controlPoint;
    }

    @Override
    public RotationPoint getRotationPoint() {
        rotationPoint.setX(startPoint.getX());
        rotationPoint.setY(startPoint.getY());
        return rotationPoint;
    }

    public boolean isPointFromLine(int x, int y) {
        return false;
    }

    @Override
    public void setLastHandlePointPosition(double x, double y) {
//        super.setLastHandlePointPosition(startPoint.getX() - x,
//                                         startPoint.getY() - y);
        double newXValue = Math.abs(x - startPoint.getX());
        double newYValue = Math.abs(y - startPoint.getY());
        super.setLastHandlePointPosition(newXValue,newYValue);
    } 
}
