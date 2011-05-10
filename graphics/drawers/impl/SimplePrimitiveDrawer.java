package graphics.drawers.impl;

import graphics.drawers.*;
import graphics.primitives.impl.RectangleOld;
import graphics.primitives.impl.Point;
import graphics.primitives.impl.Line;
import graphics.primitives.impl.Arc;
import graphics.primitives.impl.PolyLine;
import graphics.primitives.impl.Oval;
import graphics.primitives.impl.Curve;
import graphics.*;
import graphics.primitives.*;
import java.awt.Color;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import controllers.LogLevelController;
import graphics.strokes.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Calcs;
import utils.GraphicMethods;

public class SimplePrimitiveDrawer implements PrimitiveDrawer {

    private WritableRaster raster;
    private Color backgroundColor;
    private Color handlePointColor = new Color(50,50,50);

    public SimplePrimitiveDrawer(WritableRaster raster, Color backgroundColor) {
        this.raster = raster;
        this.backgroundColor = backgroundColor;
        Logger.getLogger(SimplePrimitiveDrawer.class.getName()).setLevel(LogLevelController.getLogLevelManager().getLevel());
    }

    private void paintPixel(int x, int y, int c[]) {
        if (x >= 0 && y >= 0 && x < raster.getWidth() && y < raster.getHeight()) {
            getRaster().setPixel(x, y, c);
        }
    }

    private void drawHandlePoint(HandlePoint handlePoint) {
        RectangleOld rect = new RectangleOld((int) handlePoint.getX() - HandlePoint.HANDLE_POINT_OFFSET,
                (int) handlePoint.getY() - HandlePoint.HANDLE_POINT_OFFSET,
                HandlePoint.HANDLE_POINT_SIZE,
                HandlePoint.HANDLE_POINT_SIZE);
        rect.setBorderColor(handlePoint.getBorderColor());
        drawRectangle(rect);
    }

    private void drawRotationPoint(RotationPoint rotationPoint) {
        Oval oval = new Oval(handlePointColor, (int) rotationPoint.getX(), (int) rotationPoint.getY(),
                RotationPoint.HANDLE_POINT_SIZE,
                RotationPoint.HANDLE_POINT_SIZE);
        oval.setBorderColor(rotationPoint.getBorderColor());
        drawOval(oval);
    }

    /**
     * Bressenham algorithm
     * @param line
     */
    public int ipart(double x) {
        return (int) x;
    }

    public double round(double x) {
        return ipart(x + 0.5);
    }

    public double fpart(double x) {
        return x - (int) x;
    }

    public double rfpart(double x) {
        return 1 - fpart(x);
    }

    public int[] changeBrightness(int[] color, double br) {
        return new int[]{(int) (color[0] * br),
                    (int) (color[1] * br),
                    (int) (color[2] * br)};
    }

    public void drawPoint(Point point) {
        int[] c = new int[]{point.getBorderColor().getRed(),
                            point.getBorderColor().getGreen(),
                            point.getBorderColor().getBlue()};

        int x = (int) point.getPosition().getX();
        int y = (int) point.getPosition().getY();

        if (x >= 1) {
            paintPixel(x - 1, y, changeBrightness(c, 0.4));
        }
        if (x < getRaster().getWidth()) {
            paintPixel(x + 1, y, changeBrightness(c, 0.4));
        }
        if (y >= 1) {
            paintPixel(x, y - 1, changeBrightness(c, 0.4));
        }
        if (y < getRaster().getHeight()) {
            paintPixel(x, y + 1, changeBrightness(c, 0.4));
        }

        paintPixel(x, y, c);
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color color) {
        int stepx, stepy;

        int[] c = new int[]{ color.getRed(),
                             color.getGreen(),
                             color.getBlue() };

        int dy = y1 - y0;
        int dx = x1 - x0;


        if (dy < 0) {
            dy = -dy;
            stepy = -1;
        } else {
            stepy = 1;
        }
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else {
            stepx = 1;
        }
        dy <<= 1;                                                  // dy is now 2*dy
        dx <<= 1;                                                  // dx is now 2*dx

        paintPixel(x0, y0, c);

        if (dx > dy) {
            int fraction = dy - (dx >> 1);                       // same as 2*dy - dx
            while (x0 != x1) {
                //System.out.println("x0 = " + x0 + " x1 = " + x1 + " dx / 2 = " + (x0 + (dx / 2)) + " dx = " + dx);
                if (fraction >= 0) {
                    y0 += stepy;
                    fraction -= dx;                                // same as fraction -= 2*dx
                }
                x0 += stepx;
                fraction += dy;                                    // same as fraction -= 2*dy
                paintPixel(x0, y0, c);
            }
        } else {
            int fraction = dx - (dy >> 1);
            while (y0 != y1) {
                if (fraction >= 0) {
                    x0 += stepx;
                    fraction -= dy;
                }
                y0 += stepy;
                fraction += dx;
                paintPixel(x0, y0, c);
            }
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color color, Stroke stroke) {
        int stepx, stepy;

        int[] c = new int[]{ color.getRed(),
                             color.getGreen(),
                             color.getBlue() };

        int dy = y1 - y0;
        int dx = x1 - x0;


        if (dy < 0) {
            dy = -dy;
            stepy = -1;
        } else {
            stepy = 1;
        }
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else {
            stepx = 1;
        }
        dy <<= 1;                                                  // dy is now 2*dy
        dx <<= 1;                                                  // dx is now 2*dx

        paintPixel(x0, y0, c, stroke);

        if (dx > dy) {
            int fraction = dy - (dx >> 1);                       // same as 2*dy - dx
            while (x0 != x1) {
                //System.out.println("x0 = " + x0 + " x1 = " + x1 + " dx / 2 = " + (x0 + (dx / 2)) + " dx = " + dx);
                if (fraction >= 0) {
                    y0 += stepy;
                    fraction -= dx;                                // same as fraction -= 2*dx
                }
                x0 += stepx;
                fraction += dy;                                    // same as fraction -= 2*dy
                paintPixel(x0, y0, c, stroke);
            }
        } else {
            int fraction = dx - (dy >> 1);
            while (y0 != y1) {
                if (fraction >= 0) {
                    x0 += stepx;
                    fraction -= dy;
                }
                y0 += stepy;
                fraction += dx;
                paintPixel(x0, y0, c, stroke);
            }
        }
    }

    public void drawLine(Line line) {
        int x0 = (int) line.getStartPoint().getX();
        int y0 = (int) line.getStartPoint().getY();
        int x1 = (int) line.getEndPoint().getX();
        int y1 = (int) line.getEndPoint().getY();

        drawLine(x0, y0, x1, y1, line.getBorderColor());

    }

    public void drawRectangle(RectangleOld rect) {
        drawLine((int)rect.getUpperLeft().getX(),
                 (int)rect.getUpperLeft().getY(),
                 (int)rect.getUpperRight().getX(),
                 (int)rect.getUpperRight().getY(),
                 rect.getBorderColor());

        drawLine((int)rect.getUpperLeft().getX(),
                 (int)rect.getUpperLeft().getY(),
                 (int)rect.getBottomLeft().getX(),
                 (int)rect.getBottomLeft().getY(),
                 rect.getBorderColor());

        drawLine((int)rect.getBottomRight().getX(),
                 (int)rect.getBottomRight().getY(),
                 (int)rect.getUpperRight().getX(),
                 (int)rect.getUpperRight().getY(),
                 rect.getBorderColor());

        drawLine((int)rect.getBottomRight().getX(),
                 (int)rect.getBottomRight().getY(),
                 (int)rect.getBottomLeft().getX(),
                 (int)rect.getBottomLeft().getY(),
                 rect.getBorderColor());
    }

    public void drawOval(Oval oval) {

        int[] c = new int[]{oval.getBorderColor().getRed(),
                            oval.getBorderColor().getGreen(),
                            oval.getBorderColor().getBlue()};

        int x;
        int y;
        int xchange;
        int ychange;
        int ellipseError;
        int twoASquare;
        int twoBSquare;
        int stoppingX;
        int stoppingY;
        int xRadius = (int) oval.getControlPoint().getX();
        int yRadius = (int) oval.getControlPoint().getY();

        if ((xRadius != 0) && (yRadius != 0) ) {
            twoASquare = 2 * xRadius * xRadius;
            twoBSquare = 2 * yRadius * yRadius;

            x = xRadius;
            y = 0;

            xchange = yRadius * yRadius * (1 - 2 * xRadius);
            ychange = xRadius * xRadius;

            ellipseError = 0;

            stoppingX = twoBSquare * xRadius;
            stoppingY = 0;

            while (stoppingX >= stoppingY) {
//                paintPixel((int) oval.getStartPoint().getX() + x, (int) oval.getStartPoint().getY() + y, c);
//                paintPixel((int) oval.getStartPoint().getX() - x, (int) oval.getStartPoint().getY() + y, c);
//                paintPixel((int) oval.getStartPoint().getX() - x, (int) oval.getStartPoint().getY() - y, c);
//                paintPixel((int) oval.getStartPoint().getX() + x, (int) oval.getStartPoint().getY() - y, c);
//                paintPixel(x,y, c);
//                paintPixel(-x,y, c);
//                paintPixel(-x,-y, c);
//                paintPixel(x,-y, c);
                paintPixel((int) oval.getStartPoint().getX() + x, (int) oval.getStartPoint().getY() + y, c);
                paintPixel((int) oval.getStartPoint().getX() - x, (int) oval.getStartPoint().getY() + y, c);
                paintPixel((int) oval.getStartPoint().getX() - x, (int) oval.getStartPoint().getY() - y, c);
                paintPixel((int) oval.getStartPoint().getX() + x, (int) oval.getStartPoint().getY() - y, c);
                y++;
                stoppingY += twoASquare;
                ellipseError += ychange;
                ychange += twoASquare;
                if ((2 * ellipseError + xchange) > 0) {
                    x--;
                    stoppingX -= twoBSquare;
                    ellipseError += xchange;
                    xchange += twoBSquare;
                }
            }

            x = 0;
            y = yRadius;

            xchange = yRadius * yRadius;
            ychange = xRadius * xRadius * (1 - 2 * yRadius);

            ellipseError = 0;

            stoppingX = 0;
            stoppingY = twoASquare * yRadius;

            while (stoppingX <= stoppingY) {
                paintPixel((int) oval.getStartPoint().getX() + x, (int) oval.getStartPoint().getY() + y, c);
                paintPixel((int) oval.getStartPoint().getX() - x, (int) oval.getStartPoint().getY() + y, c);
                paintPixel((int) oval.getStartPoint().getX() - x, (int) oval.getStartPoint().getY() - y, c);
                paintPixel((int) oval.getStartPoint().getX() + x, (int) oval.getStartPoint().getY() - y, c);
                
                x++;
                stoppingX += twoBSquare;
                ellipseError += xchange;
                xchange += twoBSquare;
                if ((2 * ellipseError + ychange) > 0) {
                    y--;
                    stoppingY -= twoASquare;
                    ellipseError += ychange;
                    ychange += twoASquare;
                }
            }
        }
    }

    public void drawPolyLine(PolyLine polyLine) {
        Iterator it = polyLine.getHandlePoints().iterator();

        HandlePoint firstTemp = (HandlePoint) it.next();
        HandlePoint secondTemp;

        while ( it.hasNext() ) {
            secondTemp = (HandlePoint) it.next();
            drawLine( (int) firstTemp.getX(),
                      (int) firstTemp.getY(),
                      (int) secondTemp.getX(),
                      (int) secondTemp.getY(),
                      polyLine.getBorderColor());

            firstTemp = secondTemp;
        }

        secondTemp = polyLine.getFirstAddedHandlePoint();

        if ( polyLine.isClosed() ) {
            drawLine( (int) firstTemp.getX(),
                      (int) firstTemp.getY(),
                      (int) secondTemp.getX(),
                      (int) secondTemp.getY(),
                      polyLine.getBorderColor());
        }
    }

    public void drawCurve(Curve curve) {
        double a;
        double b;
        
        HandlePoint beg;
        HandlePoint end;
        HandlePoint point = new HandlePoint(curve, 0, 0);
        int color[];

        switch (curve.getHandlePoints().size()) {

            case 0: break;
            case 1: break;
            case 2:
//                beg = curve.getStartPoint();
//                end = curve.getEndPoint();
//                Line line = new Line(beg, end);
//                line.setBorderColor(curve.getBorderColor());
//                drawLine(line);
//                break;
            case 3:
                // QUADRATIC BEZIER
                beg = curve.getStartPoint();
                end = curve.getEndPoint();
                HandlePoint cp = curve.getLastAddedHandlePoint();

                color = new int[]{curve.getBorderColor().getRed(),
                                  curve.getBorderColor().getGreen(),
                                  curve.getBorderColor().getBlue()};

                for (double t = 0; t <= 1; t += 0.0001) {
                    a = t;
                    b = 1 - t;

                    point.setX(beg.getX() * b * b +
                            2 * cp.getX() * b * a +
                            end.getX() * a * a);

                    point.setY(beg.getY() * b * b +
                            2 * cp.getY() * b * a +
                            end.getY() * a * a);

                    paintPixel((int) point.getX(), (int) point.getY(), color);
                }

                break;
            default:
                // CUBIC BEZIER
                beg = curve.getStartPoint();
                end = curve.getEndPoint();
                HandlePoint cp1 = curve.getHandlePoints().get(2);
                HandlePoint cp2 = curve.getHandlePoints().get(3);

                color = new int[]{curve.getBorderColor().getRed(),
                                  curve.getBorderColor().getGreen(),
                                  curve.getBorderColor().getBlue()};

                for (double t = 0; t <= 1; t += 0.0001) {
                    a = t;
                    b = 1 - t;

                    point.setX(beg.getX() * b * b * b +
                            3 * cp1.getX() * b * b * a +
                            3 * cp2.getX() * b * a * a +
                            end.getX() * a * a * a);

                    point.setY(beg.getY() * b * b * b +
                            3 * cp1.getY() * b * b * a +
                            3 * cp2.getY() * b * a * a +
                            end.getY() * a * a * a);
                    paintPixel((int) point.getX(), (int) point.getY(), color);
                }
                break;
        }
    }

    public void drawBezier(HandlePoint hp, double t) {

    }

    /**
     * @return the raster
     */
    public WritableRaster getRaster() {
        return raster;
    }

    /**
     * @param raster the raster to set
     */
    public void setRaster(WritableRaster raster) {
        this.raster = raster;
    }

    public void eraseDrawable(Drawable drawable) {
        Logger.getLogger(SimplePrimitiveDrawer.class.getName()).
                log(Level.INFO, "Erasing " + drawable);

        Color originalColor = drawable.getBorderColor();
        drawable.setBorderColor(backgroundColor);

        if (drawable instanceof Point) {
            drawPoint((Point)drawable);
        } else if (drawable instanceof Line) {
            drawLine((Line)drawable);
        } else if (drawable instanceof RectangleOld) {
            drawRectangle((RectangleOld)drawable);
        } else if (drawable instanceof Oval) {
            drawOval((Oval)drawable);
        } else if (drawable instanceof Curve) {
            drawCurve((Curve)drawable);
        } else if (drawable instanceof PolyLine) {
            //drawPolyLine((PolyLine)drawable);
            fillPolyLine((PolyLine)drawable);
        } else if (drawable instanceof Arc){
            drawArc((Arc)drawable);
        } else if (drawable instanceof HandlePoint) {
            drawHandlePoint((HandlePoint)drawable);
        } else if (drawable instanceof RotationPoint) {
            drawRotationPoint((RotationPoint)drawable);
        }

        if (drawable instanceof GraphicPrimitive) {
            if (((GraphicPrimitive)drawable).isSelected()) {
                for (HandlePoint hp : ((GraphicPrimitive)drawable).getHandlePoints()) {
                    hp.setBorderColor(backgroundColor);
                    drawHandlePoint(hp);
                    Logger.getLogger(SimplePrimitiveDrawer.class.getName()).
                                log(Level.INFO, hp + " drawed");
                }
                RotationPoint rp = ((GraphicPrimitive)drawable).getRotationPoint();
                rp.setBorderColor(backgroundColor);
                drawRotationPoint(rp);
                Logger.getLogger(SimplePrimitiveDrawer.class.getName()).
                log(Level.INFO, rp + " drawed");
            }
        }

        drawable.setBorderColor(originalColor);
    }

    public void setRaster(Raster raster) {
        this.raster = (WritableRaster) raster;
    }

    /**
     * @return the backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void draw(GraphicPrimitive gp) {
        if (gp instanceof Point) {
            drawPoint((Point) gp);
        } else if (gp instanceof Line) {
            drawLine((Line) gp);
        } else if (gp instanceof RectangleOld) {
            drawRectangle((RectangleOld) gp);
        } else if (gp instanceof Oval) {
            drawOval((Oval) gp);
        } else if (gp instanceof Curve) {
            drawCurve((Curve) gp);
        } else if (gp instanceof PolyLine) {
            //drawPolyLine((PolyLine) gp);
            fillPolyLine((PolyLine) gp);
        } else if (gp instanceof Arc) {
            drawArc((Arc) gp);
        } else {
            System.out.println("Canvas: I don't know how to draw a " + gp.getClass());
        }

        Logger.getLogger(SimplePrimitiveDrawer.class.getName()).
                log(Level.INFO, gp + " drawed");

        if (gp.isSelected()) {
            for (HandlePoint hp : gp.getHandlePoints()) {
                drawHandlePoint(hp);
                Logger.getLogger(SimplePrimitiveDrawer.class.getName()).
                                log(Level.INFO, hp + " drawed");
            }
            drawRotationPoint(gp.getRotationPoint());
            Logger.getLogger(SimplePrimitiveDrawer.class.getName()).
                log(Level.INFO, gp.getRotationPoint() + " drawed");
        }

    }

    public void drawGrid(Grid grid) {
        for (int i = 0; i < raster.getHeight(); i += grid.gethSpacing()) {
//            drawLine(i, 0, i, raster.getHeight(), grid.getLineColor(), grid.getStroke());
            drawHorizontalLine(i, grid.getLineColor(), grid.getStroke());
        }
        for (int i = 0; i < raster.getWidth(); i += grid.getvSpacing()) {
//            drawLine(0, i, raster.getWidth(), i, grid.getLineColor(), grid.getStroke());
            drawVerticalLine(i, grid.getLineColor(), grid.getStroke());
        }
    }

    private void paintPixel(int x0, int y0, int[] c, Stroke stroke) {
        if (stroke.drawNextPoint()) {
            paintPixel(x0, y0, c);
        }
    }

    public void drawHorizontalLine(int y, Color color, Stroke stroke) {
        int[] c = new int[]{color.getRed(),
                            color.getGreen(),
                            color.getBlue()};
        for (int i = 0; i < raster.getWidth(); i++) {
            paintPixel(i, y, c, stroke);
        }
    }

    public void drawVerticalLine(int x, Color color, Stroke stroke) {
        int[] c = new int[]{color.getRed(),
                            color.getGreen(),
                            color.getBlue()};
        for (int i = 0; i < raster.getHeight(); i++) {
            paintPixel(x, i, c, stroke);
        }
    }

    public void drawBackground(Color backgroundColor) {
        int [] c = new int[]{
                            backgroundColor.getRed(),
                            backgroundColor.getGreen(),
                            backgroundColor.getBlue() };

        for (int y = 0; y < raster.getHeight(); y++) {
            for (int x = 0; x < raster.getWidth(); x++) {
                raster.setPixel(x, y, c);
            }
        }
    }

    public void fillPolyLine(PolyLine polyLine) {
        if (!polyLine.isClosed()) {
            drawPolyLine(polyLine);
        } else {
            int [] c = new int[]{
                            polyLine.getBorderColor().getRed(),
                            polyLine.getBorderColor().getGreen(),
                            polyLine.getBorderColor().getBlue() };

            for (int y = (int)polyLine.getMostUpY(); y <= (int)polyLine.getMostBottomY(); y++) {
                for (int x = (int)polyLine.getMostLeftX(); x <= (int)polyLine.getMostRightX(); x++) {
                    if (GraphicMethods.pointInPolygon(polyLine, x, y)) {
                        paintPixel(x, y, c);
                    }
                }
            }
            drawPolyLine(polyLine);
        }
    }

//    private void drawArc(Arc arc) {
//        if (arc.getHandlePoints().size() >= 3) {
//            int[] c = new int[]{
//                arc.getBorderColor().getRed(),
//                arc.getBorderColor().getGreen(),
//                arc.getBorderColor().getBlue()};
//
//            double cx = arc.getCenter().getX();
//            double cy = arc.getCenter().getY();
//
//            double x;
//            double y;
//
//            double radius = arc.getRadius();
//
//            double PI = Math.PI;
//            double from = arc.getAngleOffset();
//            double to = from + arc.getAngle();
//
//            drawLine((int)arc.getHandlePoints().get(0).getX(),
//                     (int)arc.getHandlePoints().get(0).getY(),
//                     (int)arc.getHandlePoints().get(1).getX(),
//                     (int)arc.getHandlePoints().get(1).getY(),
//                     Color.RED);
//
//            drawLine((int)arc.getHandlePoints().get(1).getX(),
//                     (int)arc.getHandlePoints().get(1).getY(),
//                     (int)arc.getHandlePoints().get(2).getX(),
//                     (int)arc.getHandlePoints().get(2).getY(),
//                     Color.RED);
//
//            drawLine((int)arc.getHandlePoints().get(2).getX(),
//                     (int)arc.getHandlePoints().get(2).getY(),
//                     (int)arc.getHandlePoints().get(0).getX(),
//                     (int)arc.getHandlePoints().get(0).getY(),
//                     Color.RED);
//
//            for (double angle = from; angle < to; angle += 0.01) {
//                int q = (int) (angle / (PI / 2));
//                System.out.println("Angle: " + angle + "Q: " + q);
//                x = cx + radius * Math.cos(angle);
//                y = cy - radius * Math.sin(angle);
//                paintPixel((int)x, (int)y, c);
//            }
//        }
//    }

    private void drawArc(Arc arc) {
//         if (arc.getHandlePoints().size() >= 3) {
             //Settings
             int startAngle = (int) arc.getStartAngle();
             int endangle = (int) arc.getEndAngle();
             int cx = (int) arc.getCenter().getX();
             int cy = (int) arc.getCenter().getY();
             int radius = (int) arc.getRadius();

//             int startAngle = 45;
//             int endangle = 180;
//             int cx = 200;
//             int cy = 200;
//             int radius = 50;

             int[] c = new int[]{
                 arc.getBorderColor().getRed(),
                 arc.getBorderColor().getGreen(),
                 arc.getBorderColor().getBlue()};

             // Standard Midpoint Circle algorithm
             int p = (5 - radius * 4) / 4;
             int x = 0;
             int y = radius;

             drawCirclePoints(cx, cy, x, y, startAngle, endangle, c);

             while (x <= y) {
                 x++;
                 if (p < 0) {
                     p += 2 * x + 1;
                 } else {
                     y--;
                     p += 2 * (x - y) + 1;
                 }
                 drawCirclePoints(cx, cy, x, y, startAngle, endangle, c);
             }
//         }
    }

     private void drawCirclePoints(int centerX, int centerY, int x, int y, int startAngle, int endAngle, int [] c) {

        // Calculate the angle the current point makes with the circle center
        int angle = (int) Math.toDegrees(Math.atan2(y, x));

        // draw the circle points as long as they lie in the range specified
        if (x < y) {
            // draw point in range 0 to 45 degrees
            if (90 - angle >= startAngle && 90 - angle <= endAngle) {
                paintPixel(centerX - y, centerY - x, c);
            }

            // draw point in range 45 to 90 degrees
            if (angle >= startAngle && angle <= endAngle) {
                paintPixel(centerX - x, centerY - y, c);
            }

            // draw point in range 90 to 135 degrees
            if (180 - angle >= startAngle && 180 - angle <= endAngle) {
                paintPixel(centerX + x, centerY - y, c);
            }

            // draw point in range 135 to 180 degrees
            if (angle + 90 >= startAngle && angle + 90 <= endAngle) {
                paintPixel(centerX + y, centerY - x, c);
            }

            // draw point in range 180 to 225 degrees
            if (270 - angle >= startAngle && 270 - angle <= endAngle) {
                paintPixel(centerX + y, centerY + x, c);
            }

            // draw point in range 225 to 270 degrees
            if (angle + 180 >= startAngle && angle + 180 <= endAngle) {
                paintPixel(centerX + x, centerY + y, c);
            }

            // draw point in range 270 to 315 degrees
            if (360 - angle >= startAngle && 360 - angle <= endAngle) {
                paintPixel(centerX - x, centerY + y, c);
            }

            // draw point in range 315 to 360 degrees
            if (angle + 270 >= startAngle && angle + 270 <= endAngle) {
                paintPixel(centerX - y, centerY + x, c);
            }
        }
     }

    @Override
    public void drawFillet(Line lineS, Line lineT) {
        HandlePoint hpA = null;
        HandlePoint hpB = null;

        Map <Double, Integer> distancesMap = new HashMap<Double, Integer>();

        double a = Calcs.getDistance(lineS.getStartPoint(), lineT.getStartPoint());
        double b = Calcs.getDistance(lineS.getStartPoint(), lineT.getEndPoint());
        double c = Calcs.getDistance(lineS.getEndPoint(), lineT.getStartPoint());
        double d = Calcs.getDistance(lineS.getEndPoint(), lineT.getEndPoint());

        distancesMap.put(a, 0);
        distancesMap.put(b, 1);
        distancesMap.put(c ,2);
        distancesMap.put(d, 3);

        double min = Math.min(a, Math.min(b, Math.min(c,d)));

        int minIndex = distancesMap.get(min);

        switch (minIndex) {
            case 0:
                hpA = lineS.getStartPoint();
                hpB = lineT.getStartPoint();
                break;
            case 1:
                hpA = lineS.getStartPoint();
                hpB = lineT.getEndPoint();
                break;
            case 2:
                hpA = lineS.getEndPoint();
                hpB = lineT.getStartPoint();
                break;
            case 3:
                hpA = lineS.getEndPoint();
                hpB = lineT.getEndPoint();
                break;
            default:
        }

        for (HandlePoint hp : Calcs.getFilletPoints(lineS, lineT, 50)) {
            Arc arc = new Arc();
            arc.setRadius(50);
            arc.setStartAngle(0);
            arc.setEndAngle(360);
            arc.addPoint((int)hp.getX(), (int)hp.getY());
            drawArc(arc);
        }
        
    }
}
