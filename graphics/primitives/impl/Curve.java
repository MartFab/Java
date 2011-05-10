package graphics.primitives.impl;

import graphics.primitives.*;
import graphics.HandlePoint;
import graphics.RotationPoint;
import java.awt.Color;
import utils.GraphicMethods;

public class Curve extends AbstractGraphicPrimitive {

    static final long serialVersionUID = 1L;

    public Curve () {
        super();
    }

    public Curve (Color color, int x, int y) {
        this();
        super.setBorderColor(color);
        addPoint(x, y);
    }

    public void addPoint(int x, int y) {
        handlePoints.add(new HandlePoint(this, x, y));
    }

    public Curve(Curve curve) {
        super();
        for (HandlePoint hp : curve.getHandlePoints()) {
            handlePoints.add( new HandlePoint(hp) );
        }
        this.setSelected(curve.isSelected());
        this.setBorderColor(curve.getBorderColor());
        this.setRotationPoint(new RotationPoint(curve.getRotationPoint()));
    }

    public boolean isPointFromLine(int x, int y) {
        return GraphicMethods.isPointInsideCurve(this, x, y);
    }

    /**
     * @return the startPoint
     */
    public HandlePoint getStartPoint() {
        return handlePoints.get(0);
    }

    /**
     * @return the endPoint
     */
    public HandlePoint getEndPoint() {
        return handlePoints.get(1);
    }

    public void removeLastPoint() {
        handlePoints.remove(handlePoints.get(handlePoints.size() - 1));
    }

    @Override
    public String toString(){
        StringBuffer strCurve = new StringBuffer("Curve");

        for (HandlePoint hp : getHandlePoints()) {
            strCurve.append(hp.toString());
        }

        return strCurve.toString();
    }
}

