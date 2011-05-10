package graphics.drawers.impl;

import graphics.drawers.*;
import graphics.Drawable;
import graphics.Grid;
import graphics.primitives.impl.Curve;
import graphics.primitives.GraphicPrimitive;
import graphics.primitives.impl.Line;
import graphics.primitives.impl.Oval;
import graphics.primitives.impl.Point;
import graphics.primitives.impl.PolyLine;
import graphics.primitives.impl.RectangleOld;
import graphics.strokes.Stroke;
import java.awt.Color;
import java.awt.image.Raster;

/**
 *
 * @author Diego
 */
public class AntiAliasedDrawer implements PrimitiveDrawer {

    public void setRaster(Raster raster) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawPoint(Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawLine(Line line) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawRectangle(RectangleOld rectangle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawOval(Oval oval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawPolyLine(PolyLine polyline) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawCurve(Curve curve) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void eraseDrawable(Drawable drawable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void draw(GraphicPrimitive gp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawGrid(Grid grid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawHorizontalLine(int y, Color color, Stroke stroke) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawVerticalLine(int x, Color color, Stroke stroke) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawBackground(Color backgroundColor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBackgroundColor(Color backgroundColor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void fillPolyLine(PolyLine polyLine) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawFillet(Line lineA, Line lineB) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
