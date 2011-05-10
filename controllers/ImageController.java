package controllers;

import graphics.primitives.GraphicPrimitive;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageController implements Serializable {

    private List<GraphicPrimitive> graphicPrimitives = new ArrayList<GraphicPrimitive>();
    private Color backgroundColor = Color.BLACK;
    private Color borderColor = Color.RED;

    public ImageController () {
        Logger.getLogger(ImageController.class.getName()).setLevel(LogLevelController.getLogLevelManager().getLevel());
    }

    /**
     * @return the graphicPrimitives
     */
    public List<GraphicPrimitive> getGraphicPrimitives() {
        return graphicPrimitives;
    }

    /**
     * @param graphicPrimitives the graphicPrimitives to set
     */
    public void setGraphicPrimitives(List<GraphicPrimitive> graphicPrimitives) {
        this.graphicPrimitives = graphicPrimitives;
    }

    void removeGraphicPrimitive(GraphicPrimitive graphicPrimitive) {
        graphicPrimitives.remove(graphicPrimitive);
    }

    void addGraphicPrimitive(GraphicPrimitive graphicPrimitive) {
        graphicPrimitives.add(graphicPrimitive);
        Logger.getLogger(ImageController.class.getName()).
                log(Level.INFO, "Graphic primitive " + graphicPrimitive + " added");
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

    /**
     * @return the borderColor
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor the borderColor to set
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

}

