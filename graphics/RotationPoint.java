package graphics;

import graphics.primitives.GraphicPrimitive;

public class RotationPoint extends HandlePoint {
    public static final int HANDLE_POINT_SIZE = 3;

    public RotationPoint(GraphicPrimitive graphicPrimitive, double x, double y){
        super(graphicPrimitive,x,y);
    }

    public RotationPoint(RotationPoint rotationPoint) {
        super(rotationPoint.getGraphicPrimitive(),
              rotationPoint.getX(),
              rotationPoint.getY());
    }

}
