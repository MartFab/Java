package graphics.primitives.impl;

import graphics.primitives.*;
import graphics.*;
import java.awt.Color;
import java.util.Iterator;
import utils.GraphicMethods;

public class PolyLine extends AbstractGraphicPrimitive {
    public final static int SNAP_RADIUS = 5;
    private boolean closed = false;
    private boolean rectangle = false;
    private double mostLeftX = Double.MAX_VALUE;
    private double mostRightX = 0;
    private double mostUpY = Double.MAX_VALUE;
    private double mostBottomY = 0;

    public PolyLine(Color color, int x, int y) {
        super();
        super.setBorderColor(color);
        handlePoints.add(new HandlePoint(this,x,y));
    }

    public PolyLine(Color color, int x, int y, boolean rectangle) {
        super();
        super.setBorderColor(color);
        this.rectangle = rectangle;
        this.closed = rectangle;
        handlePoints.add(new HandlePoint(this, x, y));
    }

    public PolyLine(PolyLine polyLine) {
        super();
        for (HandlePoint hp : polyLine.getHandlePoints()) {
            handlePoints.add( new HandlePoint(hp) );
        }
        setSelected(polyLine.isSelected());
        closed = polyLine.isClosed();
        rectangle = polyLine.isRectangle();
        setRotationPoint(new RotationPoint(polyLine.getRotationPoint()));
        setBoundaries();
    }
    
    public void addPoint(int x, int y) {
        handlePoints.add(new HandlePoint(this, x, y));
        if (x < getMostLeftX()) setMostLeftX(x);
        if (x > getMostRightX()) setMostRightX(x);
        if (y < getMostUpY()) setMostUpY(y);
        if (y > getMostBottomY()) setMostBottomY(y);
    }

    public void removeLastPoint() {
        handlePoints.remove(getLastAddedHandlePoint());
        setBoundaries();
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @param closed the closed to set
     */
    public void setClosed(boolean closed) {
        if (closed) removeLastPoint();
        this.closed = closed;
    }

    @Override
    public String toString() {
        StringBuffer points = new StringBuffer("PolyLine ");

        for (HandlePoint hp : getHandlePoints()) {
            points.append(hp.toString());
        }

        return points.toString();
    }

    /**
     * @return the firstAddedHandlePoint
     */
    public HandlePoint getFirstAddedHandlePoint() {
        //return firstAddedHandlePoint;
        return handlePoints.get(0);
    }

    public boolean isPointFromLine(int x, int y) {
        Iterator it = getHandlePoints().iterator();

        HandlePoint firstTemp = (HandlePoint) it.next();
        HandlePoint secondTemp;

        while ( it.hasNext() ) {
            secondTemp = (HandlePoint) it.next();
            if (GraphicMethods.isPointInsideLine(x, y, (int) firstTemp.getX(),
                                        (int) firstTemp.getY(),
                                        (int) secondTemp.getX(),
                                        (int) secondTemp.getY()))
                return true;

            firstTemp = secondTemp;
        }

        secondTemp = getFirstAddedHandlePoint();

        if ( isClosed() ) {
            if (GraphicMethods.isPointInsideLine(x, y, (int) firstTemp.getX(),
                                        (int) firstTemp.getY(),
                                        (int) secondTemp.getX(),
                                        (int) secondTemp.getY()))
                return true;
        }
        return false;
    }

    /**
     * @return the rectangle
     */
    public boolean isRectangle() {
        return rectangle;
    }

    /**
     * @param rectangle the rectangle to set
     */
    public void setRectangle(boolean rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void setLastHandlePointPosition(double x, double y) {
        if (!isRectangle()) {
            super.setLastHandlePointPosition(x, y);
        } else {
            HandlePoint topLeft = handlePoints.get(0);
            HandlePoint topRight = new HandlePoint (this, x, topLeft.getY());
            HandlePoint bottomLeft = new HandlePoint (this, topLeft.getX(), y);
            HandlePoint bottomRight = new HandlePoint(this, x, y);

            handlePoints.clear();

            handlePoints.add(topLeft);
            handlePoints.add(topRight);
            handlePoints.add(bottomRight);
            handlePoints.add(bottomLeft);
        }
    }

    /**
     * @return the mostLeftX
     */
    public double getMostLeftX() {
        return mostLeftX;
    }

    /**
     * @param mostLeftX the mostLeftX to set
     */
    public void setMostLeftX(double mostLeftX) {
        this.mostLeftX = mostLeftX;
    }

    /**
     * @return the mostRightX
     */
    public double getMostRightX() {
        return mostRightX;
    }

    /**
     * @param mostRightX the mostRightX to set
     */
    public void setMostRightX(double mostRightX) {
        this.mostRightX = mostRightX;
    }

    /**
     * @return the mostUpY
     */
    public double getMostUpY() {
        return mostUpY;
    }

    /**
     * @param mostUpY the mostUpY to set
     */
    public void setMostUpY(double mostUpY) {
        this.mostUpY = mostUpY;
    }

    /**
     * @return the mostBottomY
     */
    public double getMostBottomY() {
        return mostBottomY;
    }

    /**
     * @param mostBottomY the mostBottomY to set
     */
    public void setMostBottomY(double mostBottomY) {
        this.mostBottomY = mostBottomY;
    }

    private void setBoundaries() {
        for (HandlePoint hp : handlePoints) {
            if (hp.getX() < getMostLeftX()) setMostLeftX(hp.getX() );
            if (hp.getX() > getMostRightX()) setMostRightX(hp.getX() );
            if (hp.getY() < getMostUpY()) setMostUpY(hp.getY());
            if (hp.getY() > getMostBottomY()) setMostBottomY(hp.getY());
        }
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        setBoundaries();
    }

    @Override
    public void rotate(double angle) {
        super.rotate(angle);
        setBoundaries();
    }

    @Override
    public void escalate(double factor) {
        super.escalate(factor);
        setBoundaries();
    }


}
