package controllers;

import graphics.HandlePoint;
import graphics.primitives.GraphicPrimitive;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DrawingStateController {

    private DrawingController drawingController;
    /** Constants to know the type of action */
    public static final int IDLE = 0;
    public static final int CREATING = 1;
    public static final int DRAWING = 2;
    public static final int MOVING = 3;
    public static final int MOVING_HANDLE_POINT = 4;
    public static final int TRANSFORMING = 5;
    public static final int ROTATING = 6;
    private int state = IDLE;

    public DrawingStateController(DrawingController dc) {
        super();
        this.drawingController = dc;Logger.getLogger(DrawingStateController.class.getName()).setLevel(LogLevelController.getLogLevelManager().getLevel());

    }

    public int changeState(int x, int y) {
        HandlePoint handlePoint = drawingController.getHandlePointAt(x, y);
        GraphicPrimitive selectedGP = drawingController.getSelectedGraphicPrimitive();
        GraphicPrimitive gpAtXY = drawingController.getGraphicPrimitiveAt(x, y);
        GraphicPrimitive editingGP = drawingController.getEditingPrimitive();
        GraphicPrimitive rotatingGP = drawingController.getRotatingPrimitive();
        Class gpToDraw = drawingController.getSelectedGPToDraw();

        switch (state) {
            case IDLE:
                // It can be:
                //   creando creation
                //   seleccionando selection
                //   click on the control point
                if (gpToDraw != null) {
                    setState(CREATING);
                } else if (handlePoint != null) {
                    setState(TRANSFORMING);
                } else if (gpAtXY != null && gpAtXY == selectedGP) {
                    setState(MOVING);
                }
                break;
            case CREATING:

                if (gpToDraw == null) {
                    setState(IDLE);
                } else if (editingGP != null) {
                    setState(DRAWING);
                }
                break;
            case DRAWING:
                if (editingGP == null) {
                    setState(CREATING);
                }

                break;
            case MOVING:
                if (handlePoint != null) {
                    setState(TRANSFORMING);
                } else if (gpAtXY == null || gpAtXY != selectedGP) {
                    setState(IDLE);
                }
                break;
            case TRANSFORMING:
                if (handlePoint == null) {
                    if (selectedGP != null) {
                        setState(MOVING);
                    } else {
                        setState(IDLE);
                    }
                } else if (rotatingGP != null) {
                    setState(ROTATING);
                }
                break;
            case ROTATING:
                if (handlePoint == null) {
                    if (gpAtXY != null) {
                        setState(MOVING);
                    } else {
                        setState(IDLE);
                    }
                }
                break;
            default:
        }

        return getState();
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        StringBuffer stateName = new StringBuffer("New state: ");
        switch (state) {
            case DrawingStateController.IDLE:
                stateName.append("DrawingStateController.IDLE");
                break;
            case DrawingStateController.CREATING:
                stateName.append("DrawingStateController.CREATING");
                break;
            case DrawingStateController.DRAWING:
                stateName.append("DrawingStateController.DRAWING");
                break;
            case DrawingStateController.TRANSFORMING:
                stateName.append("DrawingStateController.TRANSFORMING");
                break;
            case DrawingStateController.ROTATING:
                stateName.append("DrawingStateController.ROTATING");
                break;
            case DrawingStateController.MOVING:
                stateName.append("DrawingStateController.MOVING");
                break;
            default:
        }
        Logger.getLogger(DrawingStateController.class.getName()).log(Level.INFO, stateName.toString());
    }
}
