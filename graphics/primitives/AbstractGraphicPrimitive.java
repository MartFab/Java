/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.primitives;

import graphics.primitives.impl.Point;
import graphics.primitives.impl.Line;
import graphics.primitives.impl.RectangleOld;
import graphics.primitives.impl.Arc;
import graphics.primitives.impl.PolyLine;
import graphics.primitives.impl.Oval;
import graphics.primitives.impl.Curve;
import graphics.*;
import java.awt.Color;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import controllers.LogLevelController;
import graphics.strokes.Stroke;

public abstract class AbstractGraphicPrimitive implements GraphicPrimitive, Serializable, Drawable {

    private Stroke stroke;
    private int lineWidth = 1;
    private int layer = 0;
    private boolean selected = false;
    private Color borderColor = Color.RED;
    //static final long serialVersionUID = 1L;
    protected List<HandlePoint> handlePoints = new ArrayList<HandlePoint>();
    protected RotationPoint rotationPoint = new RotationPoint(this, 0.,0.);

    /**
     * Default constructor
     */
    public AbstractGraphicPrimitive() {
        super();
        Logger.getLogger(AbstractGraphicPrimitive.class.getName()).setLevel(LogLevelController.getLogLevelManager().getLevel());
    }


    public boolean isInBounds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
     * @return the color
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @param color the color to set
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the lineWidth
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * @param lineWidth the lineWidth to set
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }


    /**
     * @return the stroke
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * @param stroke the stroke to set
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * @return the layer
     */
    public int getLayer() {
        return layer;
    }

    /**
     * @param layer the layer to set
     */
    public void setLayer(int layer) {
        this.layer = layer;
    }

//    /**
//     * @return the created
//     */
//    public Date getCreated() {
//        return created;
//    }

    public void resize() {

    }

    public void move(int x, int y) {
        HandlePoint diff = new HandlePoint(this, x, y);
        for (HandlePoint hp : handlePoints) {
            hp.addHandlePoint(diff);
        }
        getRotationPoint().addHandlePoint(diff);
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        Logger.getLogger(AbstractGraphicPrimitive.class.getName()).log(Level.INFO, this + " selected: " + true);
    }

    public Dimension getPreferredSize() {
        double maxX = 0;
        double maxY = 0;

        for (HandlePoint hp : handlePoints) {
            if (hp.getX() > maxX) {
                maxX = hp.getX();
            }
            if (hp.getY() > maxY) {
                maxY = hp.getY();
            }
        }

        return new Dimension((int)Math.ceil(maxX), (int)Math.ceil(maxY));
    }


    public List<HandlePoint> getHandlePoints() {
        return handlePoints;
    }

    /**
     * @return the middlePoint
     */
    public RotationPoint getRotationPoint() {
        return rotationPoint;
    }

    /**
     * @param middlePoint the middlePoint to set
     */
    public void setRotationPoint(RotationPoint rotationPoint) {
        this.rotationPoint = rotationPoint;
    }

    public void setRotationPoint(double x, double y) {
        rotationPoint.setX(x);
        rotationPoint.setY(y);
    }

    public void rotate (double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        for (HandlePoint hp : getHandlePoints()) {
            double traslatedX = hp.getX() - getRotationPoint().getX();
            double traslatedY = hp.getY() - getRotationPoint().getY();

            double newX = traslatedX * cos - traslatedY * sin;
            double newY = traslatedX * sin + traslatedY * cos;

            hp.setValues(getRotationPoint().getX() + newX,
                         getRotationPoint().getY() + newY);
        }
    }

    public void escalate(double factor) {
        for (HandlePoint hp : getHandlePoints()) {
            double xPixelsToMove = hp.getX() - getRotationPoint().getX();
            double yPixelsToMove = hp.getY() - getRotationPoint().getY();

            xPixelsToMove *= factor;
            yPixelsToMove *= factor;

            hp.setValues(getRotationPoint().getX() + xPixelsToMove,
                         getRotationPoint().getY() + yPixelsToMove);
        }
    }

    public void restoreRotationPoint() {
        double x = 0;
        double y = 0;

        for (HandlePoint hp : handlePoints) {
            x += hp.getX();
            y += hp.getY();
        }

        x /= handlePoints.size();
        y /= handlePoints.size();

        setRotationPoint(x,y);
    }

    public GraphicPrimitive getNewInstance() {
        GraphicPrimitive newInstance = null;

        if (this instanceof Point) {
            newInstance = new Point((Point)this);
        } else if (this instanceof Line) {
            newInstance = new Line((Line)this);
        } else if (this instanceof RectangleOld) {
            newInstance = new RectangleOld((RectangleOld)this);
        } else if (this instanceof PolyLine) {
            newInstance = new PolyLine((PolyLine)this);
        } else if (this instanceof Oval) {
            newInstance = new Oval((Oval)this);
        } else if (this instanceof Curve) {
            newInstance = new Curve((Curve)this);
        } else if (this instanceof Arc) {
            newInstance = new Arc((Arc)this);
        }

        return newInstance;
    }

    public boolean isPointInside(int x, int y) {
        return false;
    }

    @Override
    public void setLastHandlePointPosition(double x, double y) {
        this.getLastAddedHandlePoint().setValues(x, y);
    }

    public HandlePoint getLastAddedHandlePoint() {
        HandlePoint ret = null;

        if (handlePoints.size() > 0) {
            ret = handlePoints.get(handlePoints.size() - 1);
        }

        return ret;
    }

}
