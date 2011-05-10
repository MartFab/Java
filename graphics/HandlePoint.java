package graphics;

import graphics.primitives.GraphicPrimitive;
import java.awt.Color;
import java.io.Serializable;

public class HandlePoint implements Cloneable, Drawable, Serializable {

    public static final int HANDLE_POINT_SIZE = 6;
    public static final int HANDLE_POINT_OFFSET = HANDLE_POINT_SIZE / 2;
    public static final Color HANDLE_POINT_COLOR = new Color(150,150,150);
    private boolean visible = false;
    private double x;
    private double y;
    private GraphicPrimitive graphicPrimitive;
    private Color borderColor = HANDLE_POINT_COLOR;

    public HandlePoint(GraphicPrimitive graphicPrimitive, double x, double y) {
        super();
        this.x = x;
        this.y = y;
        this.graphicPrimitive = graphicPrimitive;
    }

    public HandlePoint(HandlePoint hp) {
        this(hp.getGraphicPrimitive(), hp.getX(), hp.getY());
        setBorderColor(hp.getBorderColor());
    }

    public void setValues(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setValues(HandlePoint hp) {
        this.x = hp.getX();
        this.y = hp.getY();
    }
    
    /**
     * @return visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible Set the handle point as visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean pointIsInside(double x, double y) {
        if (x < HANDLE_POINT_OFFSET + this.x && x > this.x - HANDLE_POINT_OFFSET ) {
            if (y < HANDLE_POINT_OFFSET + this.y && y > this.y - HANDLE_POINT_OFFSET) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the graphicPrimitive
     */
    public GraphicPrimitive getGraphicPrimitive() {
        return graphicPrimitive;
    }

    /**
     * @param graphicPrimitive the graphicPrimitive to set
     */
    public void setGraphicPrimitive(GraphicPrimitive graphicPrimitive) {
        this.graphicPrimitive = graphicPrimitive;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass())
            return false;
        if (this == obj)
            return true;
        if (obj instanceof HandlePoint) {
            return ((HandlePoint)obj).getX() == this.x &&
                   ((HandlePoint)obj).getY() == this.y &&
                   ((HandlePoint)obj).getGraphicPrimitive() == this.graphicPrimitive;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (53 * hash + this.x);
        hash = (int) (53 * hash + this.y);
        hash = 53 * hash + (this.graphicPrimitive != null ? this.graphicPrimitive.hashCode() : 0);
        return hash;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void addHandlePoint(HandlePoint hp) {
        this.x += hp.getX();
        this.y += hp.getY();
    }

    public void substractHandlePoint(HandlePoint hp) {
        this.x -= hp.getX();
        this.y -= hp.getY();
    }

}
