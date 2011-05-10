package graphics.primitives.impl;

import graphics.primitives.*;
import graphics.*;
import java.awt.Color;
import utils.GraphicMethods;

public class Point extends AbstractGraphicPrimitive {
    
    static final long serialVersionUID = 1L;
    private HandlePoint position;
    private double x;
    private double y;

    public Point (Color color, double x, double y) {
        super();
        super.setBorderColor(color);
        this.x = x;
        this.y = y;
        position = new HandlePoint(this, x,y);
        handlePoints.add(position);
    }

    public Point(Point point) {
        super();
        this.x = point.getPosition().getX();
        this.y = point.getPosition().getY();
        position = new HandlePoint(this, x,y);
        handlePoints.add(position);
        setSelected(point.isSelected());
        setBorderColor(point.getBorderColor());
        setRotationPoint(new RotationPoint(point.getRotationPoint()));
    }

    @Override
    public String toString() {
        return "Point (" + getPosition().getX() + "," + getPosition().getY() + ")";
    }

    /**
     * @return the position
     */
    public HandlePoint getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(HandlePoint position) {
        this.position = position;
    }

    @Override
    public RotationPoint getRotationPoint() {
        setRotationPoint(new RotationPoint(this, position.getX(), position.getY()));
        return super.getRotationPoint();
    }

    @Override
    public void rotate(double angle) {}

    public boolean isPointFromLine(int x, int y) {
        return GraphicMethods.pointInRange(x, y, (int)this.x, (int)this.y);
    }

}

