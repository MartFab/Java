package graphics.primitives.impl;

import graphics.primitives.*;
import graphics.*;
import utils.GraphicMethods;

public class RectangleOld extends AbstractGraphicPrimitive{

    static final long serialVersionUID = 1L;
    private HandlePoint upperLeft;
    private HandlePoint upperRight;
    private HandlePoint bottomLeft;
    private HandlePoint bottomRight;

    public RectangleOld(int x, int y, int width, int height) {
        upperLeft = new HandlePoint(this, x, y);
        upperRight = new HandlePoint(this, x + width, y);
        bottomLeft = new HandlePoint(this, x, y + height);
        bottomRight = new HandlePoint(this, x + width, y + height);

        handlePoints.add(upperLeft);
        handlePoints.add(upperRight);
        handlePoints.add(bottomLeft);
        handlePoints.add(bottomRight);

        restoreRotationPoint();
    }

    public RectangleOld(RectangleOld rect) {
        upperLeft = new HandlePoint(rect.getUpperLeft());
        upperRight = new HandlePoint(rect.getUpperRight());
        bottomLeft = new HandlePoint(rect.getBottomLeft());
        bottomRight = new HandlePoint(rect.getBottomRight());

        handlePoints.add(upperLeft);
        handlePoints.add(upperRight);
        handlePoints.add(bottomLeft);
        handlePoints.add(bottomRight);

        setSelected(rect.isSelected());
        setRotationPoint(new RotationPoint(rect.getRotationPoint()));
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return (int) (upperRight.getX() - upperLeft.getX());
    }

    public void setWidth(int width) {
        upperRight.setX(upperLeft.getX() + width);
        bottomRight.setX(upperLeft.getX() + width);
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return (int) (bottomLeft.getY() - upperLeft.getY());
    }

    public void setHeight(int height) {
        upperRight.setY(upperLeft.getY() + height);
        bottomRight.setY(upperLeft.getY() + height);
    }

    @Override
    public String toString() {
        return "Rectangle " + upperLeft + upperRight + bottomLeft + bottomRight;
    }


    /**
     * @return the upperLeft
     */
    public HandlePoint getUpperLeft() {
        return upperLeft;
    }

    /**
     * @param upperLeft the upperLeft to set
     */
    public void setUpperLeft(HandlePoint upperLeft) {
        this.upperLeft.setValues(upperLeft);
        getBottomLeft().setX(upperLeft.getX());
        getUpperRight().setY(upperLeft.getY());
    }

    /**
     * @return the upperRight
     */
    public HandlePoint getUpperRight() {
        return upperRight;
    }

    /**
     * @param upperRight the upperRight to set
     */
    public void setUpperRight(HandlePoint upperRight) {
        this.upperRight.setValues(upperRight);
        getBottomRight().setX(upperRight.getX());
        getUpperLeft().setY(upperRight.getY());
    }

    /**
     * @return the bottomLeft
     */
    public HandlePoint getBottomLeft() {
        return bottomLeft;
    }

    /**
     * @param bottomLeft the bottomLeft to set
     */
    public void setBottomLeft(HandlePoint bottomLeft) {
        this.bottomLeft.setValues(bottomLeft);
        getUpperRight().setX(bottomLeft.getX());
        getBottomRight().setY(bottomLeft.getY());
    }

    /**
     * @return the bottomRight
     */
    public HandlePoint getBottomRight() {
        return bottomRight;
    }

    /**
     * @param bottomRight the bottomRight to set
     */
    public void setBottomRight(HandlePoint bottomRight) {
        this.bottomRight.setValues(bottomRight);
        getUpperRight().setX(bottomRight.getX());
        getBottomLeft().setY(bottomRight.getY());
    }

    @Override
    public RotationPoint getRotationPoint() {
        return super.getRotationPoint();
    }

    public boolean isPointFromLine(int x, int y) {
        return (GraphicMethods.isPointInsideLine(x, y, (int) getUpperLeft().getX(),
                                        (int) getUpperLeft().getY(),
                                        (int) getBottomLeft().getX(),
                                        (int) getBottomLeft().getY()) ||
                GraphicMethods.isPointInsideLine(x, y, (int) getBottomLeft().getX(),
                                        (int) getBottomLeft().getY(),
                                        (int) getBottomRight().getX(),
                                        (int) getBottomRight().getY()) ||
                GraphicMethods.isPointInsideLine(x, y, (int) getBottomRight().getX(),
                                        (int) getBottomRight().getY(),
                                        (int) getUpperRight().getX(),
                                        (int) getUpperRight().getY()) ||
                GraphicMethods.isPointInsideLine(x, y, (int) getUpperRight().getX(),
                                        (int) getUpperRight().getY(),
                                        (int) getUpperLeft().getX(),
                                        (int) getUpperLeft().getY())
                                        );
    }
}

