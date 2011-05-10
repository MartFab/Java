package graphics.drawers;

import graphics.primitives.impl.Point;
import graphics.primitives.impl.Line;
import graphics.primitives.impl.RectangleOld;
import graphics.primitives.impl.PolyLine;
import graphics.primitives.impl.Oval;
import graphics.primitives.impl.Curve;
import graphics.*;
import graphics.primitives.*;
import java.awt.Color;
import java.awt.image.Raster;
import graphics.strokes.Stroke;

public interface PrimitiveDrawer {

    public void setRaster(Raster raster);

    public void drawPoint(Point point);
    public void drawLine(Line line);
    public void drawRectangle(RectangleOld rectangle);
    public void drawOval(Oval oval);
    public void drawPolyLine(PolyLine polyline);
    public void drawCurve(Curve curve);

    public void eraseDrawable(Drawable drawable);

    public void draw(GraphicPrimitive gp);

    public void drawGrid(Grid grid);

    public void drawHorizontalLine(int y, Color color, Stroke stroke);
    public void drawVerticalLine(int x, Color color, Stroke stroke);

    public void drawBackground(Color backgroundColor);

    public void setBackgroundColor(Color backgroundColor);

    public void fillPolyLine(PolyLine polyLine);

    public void drawFillet(Line lineA, Line lineB);
}
