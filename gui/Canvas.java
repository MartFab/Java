package gui;

import events.GraphicPrimitiveChangeEvent;
import graphics.primitives.GraphicPrimitive;
import controllers.ImageController;
import events.GraphicPrimitiveChangeListener;
import graphics.Drawable;
import graphics.drawers.PrimitiveDrawer;
import graphics.drawers.impl.SimplePrimitiveDrawer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import controllers.LogLevelController;
import graphics.Grid;
import graphics.strokes.impl.IntermitentStroke;
import graphics.primitives.impl.Line;
import graphics.strokes.impl.ContinuousStroke;
import java.awt.Color;

public class Canvas extends JPanel implements GraphicPrimitiveChangeListener {

    /** Singleton holding variable */
    private static Canvas canvas;
    /** Canvas constants */
    private static final int MARGIN = 10;
    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 400;
    private int currentWidth = 640;
    private int currentHeight = 480;
    private boolean initialized;
    private boolean gridEnabled = true;
    private Color borderColor = Color.RED;
    private Color backgroundColor = Color.BLACK;
    private PrimitiveDrawer primitiveDrawer;
    private ImageController imageController;
    private BufferedImage bufferedImage;
    private WritableRaster raster;
    private Grid grid;
    private Line xGuide;
    private Line yGuide;

//    Line lineA = new Line(Color.BLUE, 0, 0, 600, 600);
//    Line lineB = new Line(Color.BLUE, 0, 600, 600, 0);

    private Canvas() {
        super();
        Logger.getLogger(Canvas.class.getName()).setLevel(LogLevelController.getLogLevelManager().getLevel());
        imageController = new ImageController();
        primitiveDrawer = new SimplePrimitiveDrawer(raster, imageController.getBackgroundColor());
        initialized = false;
        grid = new Grid();
        xGuide = new Line(new Color(50,50,50),0,0,0,0);
        yGuide = new Line(new Color(50,50,50), 0,0,0,0);
//        xGuide.setBorderColor(new Color(50,50,50));
//        yGuide.setBorderColor(new Color(50,50,50));
        initialize();

//        imageController.getGraphicPrimitives().add(lineA);
//        imageController.getGraphicPrimitives().add(lineB);
    }

    public static Canvas getCanvasInstance() {
        if (canvas == null) {
            canvas = new Canvas();
            canvas.initialize();
        }
        return canvas;
    }

    public void initialize() {
        currentWidth = Math.max(getSize().width, currentWidth);
        currentHeight = Math.max(getSize().height, currentHeight);

        setImage(new BufferedImage(currentWidth, currentHeight, BufferedImage.TYPE_INT_RGB));
        setRaster(((BufferedImage) getImage()).getRaster());
        primitiveDrawer.setRaster(getRaster());

        setInitialized(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        cleanRaster();
        drawAll();

        g.drawImage(getImage(), 0, 0, null);
    }

    /**
     * @return the imageController
     */
    public ImageController getImageController() {
        return imageController;
    }

    /**
     * @param imageController the imageController to set
     */
    public void setImageController(ImageController imageController) {
        this.imageController = imageController;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return bufferedImage;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.bufferedImage = (BufferedImage) image;
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

    public void cleanRaster() {
        WritableRaster wr = raster.createCompatibleWritableRaster();
        raster.setDataElements(0, 0, wr);
    }

    public void newRaster() {
        imageController.getGraphicPrimitives().clear();
        cleanRaster();
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public Dimension getPreferredSize() {
        int w = 0;
        int h = 0;

        for (GraphicPrimitive gp : imageController.getGraphicPrimitives()) {
            if (gp.getPreferredSize().getWidth() > w) {
                w = (int) gp.getPreferredSize().getWidth();
            }
            if (gp.getPreferredSize().getHeight() > h) {
                h = (int) gp.getPreferredSize().getHeight();
            }
        }

        w = Math.max(CANVAS_WIDTH, w) + MARGIN;
        h = Math.max(CANVAS_HEIGHT, h) + MARGIN;

        return new Dimension(w,h);
    }

    @Override
    public void update(Graphics g) {
        //super.update(g);
    }

    public void resize() {
        currentWidth = Math.max(getSize().width, currentWidth);
        currentHeight = Math.max(getSize().height, currentHeight);

        setImage(new BufferedImage(currentWidth,
                currentHeight,
                BufferedImage.TYPE_INT_RGB));

        setRaster(((BufferedImage) getImage()).getRaster());
        primitiveDrawer.setRaster(getRaster());
    }

    /**
     * @return the primitiveDrawer
     */
    public PrimitiveDrawer getPrimitiveDrawer() {
        return primitiveDrawer;
    }

    /**
     * @param primitiveDrawer the primitiveDrawer to set
     */
    public void setPrimitiveDrawer(SimplePrimitiveDrawer primitiveDrawer) {
        this.primitiveDrawer = primitiveDrawer;
    }

    public void handleGraphicPrimitiveChange(GraphicPrimitiveChangeEvent evt) {
        Logger.getLogger(Canvas.class.getName()).
                log(Level.INFO, "Graphic primitive " + evt.getSource() + " changed");

        GraphicPrimitive gp = (GraphicPrimitive) evt.getNewValue();

        // Erase the unactualized primitive
        if (evt.getOldValue() != null) {
            primitiveDrawer.eraseDrawable((Drawable) evt.getOldValue());
        }

        // Redraw all the primitives to avoid blank spaces
        drawAll();
    }

    public void drawAll() {
        if ( isGridEnabled() ) primitiveDrawer.drawGrid(getGrid());
        primitiveDrawer.drawVerticalLine((int) yGuide.getStartPoint().getX(),xGuide.getBorderColor(), new ContinuousStroke());
        primitiveDrawer.drawHorizontalLine((int) xGuide.getStartPoint().getY(),xGuide.getBorderColor(),new ContinuousStroke());
        for (GraphicPrimitive gp : getImageController().getGraphicPrimitives()) {
            primitiveDrawer.draw(gp);
        }
//        primitiveDrawer.drawFillet(lineA, lineB);
    }

    public void setMousePosition(int x, int y) {
        primitiveDrawer.eraseDrawable(xGuide);
        primitiveDrawer.eraseDrawable(yGuide);

        xGuide.getStartPoint().setValues(0,y);
        xGuide.getEndPoint().setValues(this.getWidth(),y);

        yGuide.getStartPoint().setValues(x,0);
        yGuide.getEndPoint().setValues(x, this.getHeight());
    }

    /**
     * @return the grid
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
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
        this.imageController.setBorderColor(borderColor);
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
        this.imageController.setBackgroundColor(backgroundColor);
        this.primitiveDrawer.setBackgroundColor(backgroundColor);
        this.primitiveDrawer.drawBackground(backgroundColor);
    }

    /**
     * @return the gridEnabled
     */
    public boolean isGridEnabled() {
        return gridEnabled;
    }

    /**
     * @param gridEnabled the gridEnabled to set
     */
    public void setGridEnabled(boolean gridEnabled) {
        this.gridEnabled = gridEnabled;
    }
}
