package graphics;

import graphics.strokes.impl.IntermitentStroke;
import graphics.strokes.Stroke;
import java.awt.Color;

public class Grid {

    private int vSpacing = 10;
    private int hSpacing = 10;
    private Color lineColor = new Color(40,40,40);
    private Stroke stroke = new IntermitentStroke();

  public Grid(){
    super();
  }

    /**
     * @return the vSpacing
     */
    public int getvSpacing() {
        return vSpacing;
    }

    /**
     * @param vSpacing the vSpacing to set
     */
    public void setvSpacing(int vSpacing) {
        this.vSpacing = vSpacing;
    }

    /**
     * @return the hSpacing
     */
    public int gethSpacing() {
        return hSpacing;
    }

    /**
     * @param hSpacing the hSpacing to set
     */
    public void sethSpacing(int hSpacing) {
        this.hSpacing = hSpacing;
    }

    /**
     * @return the lineColor
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
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

    public int getNearestGridPointX(int x) {
        return (int) (Math.round(x / (hSpacing * 1.0)) * hSpacing);
    }

    public int getNearestGridPointY(int y) {
        return (int) (Math.round(y / (vSpacing * 1.0)) * vSpacing);
    }

}
