package factories;

import graphics.primitives.impl.Point;
import graphics.primitives.impl.Line;
import graphics.primitives.impl.Arc;
import graphics.primitives.impl.PolyLine;
import graphics.primitives.impl.Rectangle;
import graphics.primitives.impl.Oval;
import graphics.primitives.impl.Curve;
import graphics.primitives.*;
import java.awt.Color;

public class GPFactory {

    private static GPFactory factory;

    private GPFactory() {
        super();
    }

    public static GPFactory getInstance() {
        if (factory == null) {
            return new GPFactory();
        }
        return factory;
    }

    public GraphicPrimitive createGraphicPrimitive(Class gpType, Color color, int x, int y) {
        GraphicPrimitive graphicPrimitive = null;
        if (gpType == Point.class) {
            graphicPrimitive = new Point(color, x, y);
        } else if (gpType == Line.class) {
            graphicPrimitive = new Line(color, x, y, x, y);
        } else if (gpType == Rectangle.class) {
            graphicPrimitive = new PolyLine(color, x, y, true);
        } else if (gpType == Oval.class) {
            graphicPrimitive = new Oval(color, x, y, 1, 1);
        } else if (gpType == PolyLine.class) {
            graphicPrimitive = new PolyLine(color, x, y);
        } else if (gpType == Curve.class) {
            graphicPrimitive = new Curve(color, x, y);
        } else if (gpType == Arc.class) {
            graphicPrimitive = new Arc(color, x, y);
        }
        return graphicPrimitive;
    }
}
