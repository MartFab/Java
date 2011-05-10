package graphics.primitives;

import graphics.*;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

public interface GraphicPrimitive {

    public void resize ();

    public void rotate (double angle);

    public void move (int x, int y);

    public void setBorderColor(Color borderColor);

    public Color getBorderColor();

    public int getLineWidth();

    public Dimension getPreferredSize();

    public boolean isSelected();

    public void setSelected(boolean selected);

    public List<HandlePoint> getHandlePoints();

    public RotationPoint getRotationPoint();

    public void restoreRotationPoint();

    public GraphicPrimitive getNewInstance();

    public void setLastHandlePointPosition(double x, double y);

    public void escalate(double factor);

    public boolean isPointFromLine(int x, int y);

    public boolean isPointInside(int x, int y);
}

