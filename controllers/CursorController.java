package controllers;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JComponent;

public class CursorController {
    private JComponent component;
    public static Cursor DEFAULT = Cursor.getDefaultCursor();
    public static Cursor MOVE;
    public static Cursor RESIZE_SWNE;
    public static Cursor RESIZE_SENW;
    public static Cursor CROSS;
    public static Cursor ROTATE;

    private Cursor currentCursor;

    public CursorController(JComponent component) {
        this.component = component;
        loadCursors();
    }

    /**
     * @return the component
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * @param component the component to set
     */
    public void setComponent(JComponent component) {
        this.component = component;
    }

    public void setCursor(Cursor cursor) {
        currentCursor = cursor;
        component.setCursor(cursor);
    }

    private Cursor getCursorFromFile(String path, String name, int xoff, int yoff) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = new javax.swing.ImageIcon(getClass().getResource(path)).getImage();
        Point hotSpot = new Point(xoff,yoff);
        return toolkit.createCustomCursor(image, hotSpot, name);
    }

    private void loadCursors() {
        DEFAULT = getCursorFromFile("/gui/cursors/arrow.png","Arrow",2,5);
        MOVE = getCursorFromFile("/gui/cursors/move.png","Move",16,16);
        RESIZE_SWNE = getCursorFromFile("/gui/cursors/resize_swne.png","Resize",16,16);
        RESIZE_SENW = getCursorFromFile("/gui/cursors/resize_senw.png","Resize",16,16);
        CROSS = getCursorFromFile("/gui/cursors/cross.png","Default",16,16);
        ROTATE = getCursorFromFile("/gui/cursors/rotate.png","Rotate",16,16);
    }

}
