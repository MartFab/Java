package controllers;

import graphics.primitives.impl.Point;
import graphics.primitives.impl.Arc;
import graphics.primitives.impl.PolyLine;
import graphics.primitives.impl.Curve;
import events.GraphicPrimitiveChangeEvent;
import events.GraphicPrimitiveChangeListener;
import factories.GPFactory;
import graphics.primitives.*;
import graphics.*;
import gui.Canvas;
import java.awt.event.KeyEvent;
import utils.Calcs;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;
import gui.RefreshListener;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import utils.GraphicMethods;

public class DrawingController implements MouseListener, MouseMotionListener, ComponentListener, KeyListener {

    protected EventListenerList graphicPrimitiveChangeListeners = new EventListenerList();
    private Cursor cursor = CursorController.DEFAULT;
    /** This variable contain the graphic primitive selected */
    private GraphicPrimitive selectedGraphicPrimitive = null;
    private GraphicPrimitive editingPrimitive;
    private GraphicPrimitive rotatingPrimitive = null;
    private RotationPoint rotationPoint = null;
    /** This variable contain the type of the graphic primitive to draw */
    private Class selectedGPToDraw = null;
    private Canvas canvas;
    private Set<RefreshListener> refreshableItems = new HashSet<RefreshListener>();
    private HandlePoint selectedHandlePoint = null;
    private HandlePoint lastMousePosition = null;
    private CursorController cursorController;
    private DrawingStateController drawingStateController;
    private long firstClickTime = 0;
    private boolean alignToGrid = true;
    private boolean ctrlKeyPressed = false;

    private List<GraphicPrimitive> selectedGraphicPrimitives = new ArrayList<GraphicPrimitive>();

    public DrawingController(Canvas canvas) {
        this.canvas = canvas;
        this.addGraphicChangeListener(canvas);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        cursorController = new CursorController(canvas);
        drawingStateController = new DrawingStateController(this);
        Logger.getLogger(DrawingController.class.getName()).setLevel(LogLevelController.getLogLevelManager().getLevel());
    }

    public void mousePressed(MouseEvent e) {
        selectedHandlePoint = getHandlePointAt(e.getX(), e.getY());
        lastMousePosition = new HandlePoint(null, e.getX(), e.getY());

        switch (drawingStateController.getState()) {
            case DrawingStateController.IDLE:
                setSelectedHandlePoint(getHandlePointAt(e.getX(), e.getY()));
                break;
            case DrawingStateController.CREATING:
                int x = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointX(e.getX()) : e.getX();
                int y = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointY(e.getY()) : e.getY();

                if (selectedGPToDraw != null && getEditingPrimitive() == null) {
                    setEditingPrimitive(GPFactory.getInstance().createGraphicPrimitive(selectedGPToDraw, canvas.getImageController().getBorderColor(), x, y));
                    setState(drawingStateController.changeState(e.getX(), e.getY()));
                    canvas.getImageController().addGraphicPrimitive(getEditingPrimitive());
                }
                break;
            case DrawingStateController.TRANSFORMING:
                setSelectedHandlePoint(getHandlePointAt(e.getX(), e.getY()));
                break;
            default:
        }
    }

    public void mouseClicked(MouseEvent e) {
        long clickTime = System.currentTimeMillis();
        long clickInterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");

        if ((clickTime - firstClickTime) < clickInterval + 200) {
            firstClickTime = 0;
            mouseDoubleClick(e);
        } else {
            firstClickTime = clickTime;
            mouseSingleClick(e);
        }
    }

    public void mouseSingleClick(MouseEvent e) {
        HandlePoint handlePoint = getHandlePointAt(e.getX(), e.getY());
        GraphicPrimitive gp = getGraphicPrimitiveAt(e.getX(), e.getY());
        GraphicPrimitive oldGP;
        switch (drawingStateController.getState()) {
            case DrawingStateController.IDLE:
                if (handlePoint != null) {
                    setRotatingPrimitive(handlePoint.getGraphicPrimitive());
                    rotationPoint = getRotatingPrimitive().getRotationPoint();
                    setState(drawingStateController.changeState(e.getX(), e.getY()));
                } else if (gp != null) {
                    gp.setSelected(true);
                    setSelectedGraphicPrimitive(gp);
                } else {
                    if (selectedGraphicPrimitive != null) {
                        setSelectedGraphicPrimitive(null);
                    }
                }

                break;
            case DrawingStateController.DRAWING:
                if (getEditingPrimitive() instanceof Point) {
                    setEditingPrimitive(null);
                    setState(drawingStateController.changeState(e.getX(), e.getY()));
                } else if (getEditingPrimitive() instanceof PolyLine) {
                    if (!((PolyLine)getEditingPrimitive()).isRectangle()) {
                        ((PolyLine) getEditingPrimitive()).addPoint(e.getX(), e.getY());
                    }
                } else if (getEditingPrimitive() instanceof Curve) {
                    oldGP = getEditingPrimitive().getNewInstance();
                    ((Curve) getEditingPrimitive()).addPoint(e.getX(), e.getY());
                    fireGraphicPrimitiveChanged(
                            new GraphicPrimitiveChangeEvent(oldGP, null, oldGP, getEditingPrimitive()));
                } else if (getEditingPrimitive() instanceof Arc) {
                    ((Arc)getEditingPrimitive()).addPoint(e.getX(), e.getY());
                }
                break;
            case DrawingStateController.MOVING:
                setState(drawingStateController.changeState(e.getX(), e.getY()));
                break;
            case DrawingStateController.TRANSFORMING:
                if (handlePoint != null) {
                    setRotatingPrimitive(handlePoint.getGraphicPrimitive());
                    rotationPoint = getRotatingPrimitive().getRotationPoint();
                    setState(drawingStateController.changeState(e.getX(), e.getY()));
                }
                break;
            default:
        }

    }

    public void mouseDoubleClick(MouseEvent e) {
        switch (drawingStateController.getState()) {
            case DrawingStateController.DRAWING:
                int x = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointX(e.getX()) : e.getX();
                int y = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointY(e.getY()) : e.getY();

                GraphicPrimitive oldValue;

                if (getEditingPrimitive() != null) {
                    if (!((editingPrimitive instanceof PolyLine) || (editingPrimitive instanceof Curve))) {
                        getEditingPrimitive().restoreRotationPoint();
                        setEditingPrimitive(null);
                        setState(drawingStateController.changeState(e.getX(), e.getY()));
                    } else {
                        if (getEditingPrimitive() instanceof PolyLine) {
                            HandlePoint firstAddedHP = ((PolyLine) getEditingPrimitive()).getFirstAddedHandlePoint();
                            HandlePoint currentPosition = new HandlePoint(null, x, y);

                            oldValue = getEditingPrimitive().getNewInstance();

                            if (Calcs.getDistance(currentPosition, firstAddedHP) <= PolyLine.SNAP_RADIUS) {
                                ((PolyLine) getEditingPrimitive()).setClosed(true);
                            }

                            if (!((PolyLine)getEditingPrimitive()).isRectangle()) {
                                ((PolyLine) getEditingPrimitive()).removeLastPoint();
                            }
                            getEditingPrimitive().restoreRotationPoint();

                            fireGraphicPrimitiveChanged(
                                    new GraphicPrimitiveChangeEvent(
                                    getEditingPrimitive(),
                                    "",
                                    oldValue, getEditingPrimitive()));

                            setEditingPrimitive(null);
                            setState(drawingStateController.changeState(e.getX(), e.getY()));
                        } else if (getEditingPrimitive() instanceof Curve) {
                            oldValue = getEditingPrimitive().getNewInstance();
                            ((Curve) getEditingPrimitive()).removeLastPoint();
                            getEditingPrimitive().restoreRotationPoint();
                            fireGraphicPrimitiveChanged(
                                    new GraphicPrimitiveChangeEvent(
                                    getEditingPrimitive(),
                                    "",
                                    oldValue, getEditingPrimitive()));
                            setEditingPrimitive(null);
                            setState(drawingStateController.changeState(e.getX(), e.getY()));
                        }
                    }
                }
                break;
            default:
        }
    }

    public void mouseDragged(MouseEvent e) {
        GraphicPrimitive oldValue = null;
        GraphicPrimitive eventSource = getEditingPrimitive();
        GraphicPrimitive gp = getSelectedGraphicPrimitive();
        Canvas.getCanvasInstance().setMousePosition(e.getX(), e.getY());
        switch (drawingStateController.getState()) {
            // This is the code for the creation of a primitive
            case DrawingStateController.IDLE:
                if (selectedGraphicPrimitive != null) {
                    setSelectedGraphicPrimitive(null);
                }
                break;
            case DrawingStateController.DRAWING:
                if (getEditingPrimitive() != null) {
                    int x = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointX(e.getX()) : e.getX();
                    int y = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointY(e.getY()) : e.getY();

                    oldValue = getEditingPrimitive().getNewInstance();
                    getEditingPrimitive().setLastHandlePointPosition(x, y);
                    fireDataChanged();

                    if (!oldValue.equals(editingPrimitive)) {
                        fireGraphicPrimitiveChanged(
                                new GraphicPrimitiveChangeEvent(getEditingPrimitive(),
                                "",
                                oldValue, getEditingPrimitive()));
                    }

                }
                break;

            // This is the code for the modification of the point of control
            case DrawingStateController.TRANSFORMING:
                if (gp != null) {
                    oldValue = gp.getNewInstance();
                    eventSource = gp;

                    if (gp instanceof Point) {
                        getSelectedHandlePoint().setValues(e.getX(), e.getY());
                    } else {
                        HandlePoint tempHP = new HandlePoint(null, e.getX(), e.getY());
                        HandlePoint selHP = new HandlePoint(getSelectedHandlePoint());

                        tempHP.substractHandlePoint(gp.getRotationPoint());
                        selHP.substractHandlePoint(gp.getRotationPoint());

                        double scaleFactor = Calcs.module(tempHP) / Calcs.module(selHP);

                        gp.escalate(scaleFactor);
                    }

                }
                if (eventSource != null) {
                    fireGraphicPrimitiveChanged(
                            new GraphicPrimitiveChangeEvent(eventSource,
                            "",
                            oldValue, getEditingPrimitive()));
                }

                break;
            // This is the code for the translation of the primitive
            case DrawingStateController.MOVING:
                HandlePoint tempHP = new HandlePoint(null, e.getX(), e.getY());
                tempHP.substractHandlePoint(lastMousePosition);
                oldValue = getSelectedGraphicPrimitive().getNewInstance();
                getSelectedGraphicPrimitive().move((int) tempHP.getX(), (int) tempHP.getY());
                lastMousePosition = new HandlePoint(null, e.getX(), e.getY());

                fireGraphicPrimitiveChanged(
                        new GraphicPrimitiveChangeEvent(getSelectedGraphicPrimitive(),
                        "",
                        oldValue,
                        getSelectedGraphicPrimitive()));

                break;

            // This is the code for the rotation of a primitive
            case DrawingStateController.ROTATING:
                HandlePoint temporalHP = new HandlePoint(null, e.getX(), e.getY());
                selectedGraphicPrimitive = getRotatingPrimitive();
                eventSource = selectedGraphicPrimitive;
                oldValue = selectedGraphicPrimitive.getNewInstance();

                double angle = Calcs.getAngle(temporalHP, selectedHandlePoint);


                // We are looking if we have to rotate in a way of in the other.
                int factor = (GraphicMethods.ccw(selectedGraphicPrimitive.getRotationPoint(),
                        temporalHP, selectedHandlePoint));
                angle *= factor;

                selectedGraphicPrimitive.rotate(angle);


                if (eventSource != null) {
                    fireGraphicPrimitiveChanged(
                            new GraphicPrimitiveChangeEvent(eventSource,
                            "",
                            oldValue, getEditingPrimitive()));
                }

                break;
            default:
        }
    }

    public void mouseReleased(MouseEvent e) {
        switch (drawingStateController.getState()) {
            case DrawingStateController.DRAWING:
                break;
            case DrawingStateController.TRANSFORMING:
                selectedHandlePoint = null;
                setState(drawingStateController.changeState(e.getX(), e.getY()));
                break;
            case DrawingStateController.MOVING:
                setState(drawingStateController.changeState(e.getX(), e.getY()));
                break;
            case DrawingStateController.ROTATING:
                setRotatingPrimitive(null);
                setState(drawingStateController.changeState(e.getX(), e.getY()));
                break;
            default:
        }
    }

    public void mouseMoved(MouseEvent e) {
        Object oldValue = null;
        Canvas.getCanvasInstance().setMousePosition(e.getX(), e.getY());
        switch (drawingStateController.getState()) {
            case DrawingStateController.DRAWING:
                int x = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointX(e.getX()) : e.getX();
                int y = isAlignToGrid() ? Canvas.getCanvasInstance().getGrid().getNearestGridPointY(e.getY()) : e.getY();

                if (getEditingPrimitive() != null) {
                    oldValue = getEditingPrimitive().getNewInstance();
                    getEditingPrimitive().setLastHandlePointPosition(x, y);

                    if (!oldValue.equals(editingPrimitive)) {
                        fireGraphicPrimitiveChanged(
                                new GraphicPrimitiveChangeEvent(getEditingPrimitive(),
                                "",
                                oldValue, getEditingPrimitive()));
                    }

                }
                break;
            default:
        }
        setSelectedHandlePoint(getHandlePointAt(e.getX(), e.getY()));
        setState(drawingStateController.changeState(e.getX(), e.getY()));
        fireDataChanged();
        fireRefreshNeeded();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * @return the selectedGraphicPrimitive
     */
    public GraphicPrimitive getSelectedGraphicPrimitive() {
        return selectedGraphicPrimitive;
    }

    /**
     * @param selectedGraphicPrimitive the selectedGraphicPrimitive to set
     */
    public void setSelectedGraphicPrimitive(GraphicPrimitive selectedGraphicPrimitive) {
        GraphicPrimitive oldGP = null;

        if (this.selectedGraphicPrimitive != null) {
            oldGP = (this.selectedGraphicPrimitive).getNewInstance();
            oldGP.setSelected(true);
            this.selectedGraphicPrimitive.setSelected(false);
        }

        if (selectedGraphicPrimitive != null) {
            selectedGraphicPrimitive.setSelected(true);
        }

        this.selectedGraphicPrimitive = selectedGraphicPrimitive;

        fireGraphicPrimitiveChanged(
                new GraphicPrimitiveChangeEvent(this,
                "Selection changed",
                oldGP,
                selectedGraphicPrimitive));
    }

    /**
     * @return the canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * @param canvas the canvas to set
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * @return the selectedGPToDraw
     */
    public Class getSelectedGPToDraw() {
        return selectedGPToDraw;
    }

    /**
     * @param selectedGPToDraw the selectedGPToDraw to set
     */
    public void setSelectedGPToDraw(Class selectedGPToDraw) {
        this.selectedGPToDraw = selectedGPToDraw;
        this.setState(selectedGPToDraw == null ? DrawingStateController.IDLE : DrawingStateController.CREATING);
    }

    public void componentResized(ComponentEvent e) {
        canvas.resize();
        fireRefreshNeeded();

    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void clean() {
        Canvas.getCanvasInstance().newRaster();
        Canvas.getCanvasInstance().repaint();
        setState(DrawingStateController.IDLE);
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        StringBuffer stateName = new StringBuffer("State: ");
        switch (state) {
            case DrawingStateController.IDLE:
                stateName.append("DrawingStateController.IDLE");
                cursor = CursorController.DEFAULT;
                break;

            case DrawingStateController.CREATING:
                stateName.append("DrawingStateController.CREATING");
                cursor = CursorController.CROSS;
                break;

            case DrawingStateController.DRAWING:
                stateName.append("DrawingStateController.DRAWING");
                cursor = CursorController.CROSS;
                break;

            case DrawingStateController.TRANSFORMING:
                stateName.append("DrawingStateController.TRANSFORMING");
                if (selectedHandlePoint != null) {
                    GraphicPrimitive gp = selectedHandlePoint.getGraphicPrimitive();
                    RotationPoint rp = gp.getRotationPoint();
                    if (selectedHandlePoint.getY() > rp.getY()) {
                        if (selectedHandlePoint.getX() > rp.getX()) {
                            cursor = CursorController.RESIZE_SENW;
                        } else {
                            cursor = CursorController.RESIZE_SWNE;
                        }
                    } else {
                        if (selectedHandlePoint.getX() > rp.getX()) {
                            cursor = CursorController.RESIZE_SWNE;
                        } else {
                            cursor = CursorController.RESIZE_SENW;
                        }
                    }
                }
                break;

            case DrawingStateController.ROTATING:
                stateName.append("DrawingStateController.ROTATING");
                cursor = CursorController.ROTATE;
                break;

            case DrawingStateController.MOVING:
                stateName.append("DrawingStateController.MOVING");
                cursor = CursorController.MOVE;
                break;

            default:

        }
        cursorController.setCursor(cursor);
        Logger.getLogger(DrawingController.class.getName()).
                log(Level.INFO, stateName.toString());
    }

    /**
     * @return the selectedHandlePoint
     */
    public HandlePoint getSelectedHandlePoint() {
        return selectedHandlePoint;
    }

    /**
     * @param selectedHandlePoint the selectedHandlePoint to set
     */
    public void setSelectedHandlePoint(HandlePoint selectedHandlePoint) {
        this.selectedHandlePoint = selectedHandlePoint;
        this.setState(selectedHandlePoint == null ? DrawingStateController.IDLE : DrawingStateController.TRANSFORMING);
    }

    public void addGraphicChangeListener(GraphicPrimitiveChangeListener gpcl) {
        graphicPrimitiveChangeListeners.add(GraphicPrimitiveChangeListener.class, gpcl);
    }

    public void removeGraphicChangeListener(GraphicPrimitiveChangeListener gpcl) {
        graphicPrimitiveChangeListeners.remove(GraphicPrimitiveChangeListener.class, gpcl);
    }

    public void addRefreshListener(RefreshListener rl) {
        refreshableItems.add(rl);
    }

    public void removeRefreshListener(RefreshListener rl) {
        refreshableItems.remove(rl);
    }

    private void fireRefreshNeeded() {
        for (RefreshListener rl : refreshableItems) {
            rl.refresh();
        }

    }

    private void fireDataChanged() {
        for (RefreshListener rl : refreshableItems) {
            rl.updateData();
        }

    }

    public void fireGraphicPrimitiveChanged(GraphicPrimitiveChangeEvent evt) {
        Object[] listeners = graphicPrimitiveChangeListeners.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i <
                listeners.length; i +=
                        2) {
            if (listeners[i] == GraphicPrimitiveChangeListener.class) {
                ((GraphicPrimitiveChangeListener) listeners[i + 1]).handleGraphicPrimitiveChange(evt);
            }
        }

        fireRefreshNeeded();
    }

    /** Return the control point of the coordinate x, y. If nothing, return null.*/
    public HandlePoint getHandlePointAt(int x, int y) {
        HandlePoint handlePointFound = null;
        for (GraphicPrimitive gp : canvas.getImageController().getGraphicPrimitives()) {
            if (gp.isSelected()) {
                for (HandlePoint hp : gp.getHandlePoints()) {
                    if (hp.pointIsInside(x, y)) {
                        handlePointFound = hp;
                    }

                }
            }
        }
        return handlePointFound;
    }

    public GraphicPrimitive getGraphicPrimitiveAt(int x, int y) {
        for (GraphicPrimitive gp : Canvas.getCanvasInstance().getImageController().getGraphicPrimitives()) {
            if (gp.isPointFromLine(x, y)) {
                return gp;
            }
        }
        return null;
    }

    /**
     * @return the editingPrimitive
     */
    public GraphicPrimitive getEditingPrimitive() {
        return editingPrimitive;
    }

    /**
     * @param editingPrimitive the editingPrimitive to set
     */
    public void setEditingPrimitive(GraphicPrimitive editingPrimitive) {
        this.editingPrimitive = editingPrimitive;
    }

    /**
     * @return the rotatingPrimitive
     */
    public GraphicPrimitive getRotatingPrimitive() {
        return rotatingPrimitive;
    }

    /**
     * @param rotatingPrimitive the rotatingPrimitive to set
     */
    public void setRotatingPrimitive(GraphicPrimitive rotatingPrimitive) {
        this.rotatingPrimitive = rotatingPrimitive;
    }

    public boolean isAlignToGrid() {
        return (alignToGrid && !isCtrlKeyPressed()) || (!alignToGrid && isCtrlKeyPressed());
    }

    public void setAlignToGrid(boolean state) {
        this.alignToGrid = state;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
            setCtrlKeyPressed(true);
        }
    }

    public void keyReleased(KeyEvent e) {
        setCtrlKeyPressed(false);
    }

    /**
     * @return the ctrlKeyPressed
     */
    public boolean isCtrlKeyPressed() {
        return ctrlKeyPressed;
    }

    /**
     * @param ctrlKeyPressed the ctrlKeyPressed to set
     */
    public void setCtrlKeyPressed(boolean ctrlKeyPressed) {
        this.ctrlKeyPressed = ctrlKeyPressed;
    }
}
