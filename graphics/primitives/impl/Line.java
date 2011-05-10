package graphics.primitives.impl;

import graphics.primitives.*;
import graphics.*;
import java.awt.Color;
import utils.Calcs;
import utils.GraphicMethods;

public class Line extends AbstractGraphicPrimitive {

    static final long serialVersionUID = 1L;
    private HandlePoint startPoint;
    private HandlePoint endPoint;

    public Line(Color color, int x0, int y0, int x1, int y1) {
        this(new HandlePoint(null, x0, y0), new HandlePoint(null, x1, y1));
        super.setBorderColor(color);
    }

    public Line(HandlePoint startPoint, HandlePoint endPoint) {
        super();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        startPoint.setGraphicPrimitive(this);
        endPoint.setGraphicPrimitive(this);
        handlePoints.add(startPoint);
        handlePoints.add(endPoint);
        //restoreRotationPoint();
    }

    public Line(Line line) {
        super();
        this.startPoint = new HandlePoint(this,
                                          line.getStartPoint().getX(),
                                          line.getStartPoint().getY());
        this.endPoint = new HandlePoint(this,
                                          line.getEndPoint().getX(),
                                          line.getEndPoint().getY());
        handlePoints.add(startPoint);
        handlePoints.add(endPoint);
        setBorderColor(line.getBorderColor());
        setSelected(line.isSelected());
        setRotationPoint(new RotationPoint(line.getRotationPoint()));
        //restoreRotationPoint();
    }

    @Override
    public String toString() {
        return "Line" + startPoint + endPoint;
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
     * @return the endPoint
     */
    public HandlePoint getEndPoint() {
        return endPoint;
    }

    /**
     * @param endPoint the endPoint to set
     */
    public void setEndPoint(HandlePoint endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public RotationPoint getRotationPoint() {
        return super.getRotationPoint();
    }

    public double getLength() {
        return Calcs.getDistance(startPoint, endPoint);
    }

    public boolean isPointFromLine(int x, int y) {
        return GraphicMethods.isPointInsideLine(x,y,(int)startPoint.getX(),
                                                    (int)startPoint.getY(),
                                                    (int)endPoint.getX(),
                                                    (int)endPoint.getY());
    }

    public double getSlope() {
        if (endPoint.getX() - startPoint.getX() != 0) {
            return (endPoint.getY() - startPoint.getY()) / (endPoint.getX() - startPoint.getX());
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getOffset() {
        return handlePoints.get(1).getY() - getSlope() * handlePoints.get(1).getX();
    }
}

