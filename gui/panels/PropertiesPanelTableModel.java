package gui.panels;

import graphics.primitives.impl.Curve;
import graphics.primitives.GraphicPrimitive;
import graphics.HandlePoint;
import graphics.primitives.impl.Line;
import graphics.primitives.impl.Oval;
import graphics.primitives.impl.Point;
import graphics.primitives.impl.PolyLine;
import graphics.primitives.impl.RectangleOld;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class PropertiesPanelTableModel extends AbstractTableModel {

    private GraphicPrimitive gp;
    private List<Object[]> propertiesList = new ArrayList<Object[]>();
    private final String [] columnNames = new String[]{"Propiedad", "Valor"};
    private static final int COMMON_PROPERTIES_COUNT = 2;

    public PropertiesPanelTableModel() {
        super();
    }

    public PropertiesPanelTableModel(GraphicPrimitive gp) {
        super();
        setGraphicPrimitive(gp);
    }

    public int getRowCount() {
        return propertiesList.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Object[])propertiesList.get(rowIndex))[columnIndex];
    }

    /**
     * @return the gp
     */
    public GraphicPrimitive getGraphicPrimitive() {
        return gp;
    }

    /**
     * @param gp the gp to set
     */
    public void setGraphicPrimitive(GraphicPrimitive gp) {
        propertiesList.clear();

        if (gp != null) {
            propertiesList.add(new Object[]{"COLOR", gp.getBorderColor()});
            propertiesList.add(new Object[]{"ANCHO DE LINEA", gp.getLineWidth()});

            this.gp = gp;

            if (gp instanceof Point) {
                propertiesList.add(new Object[]{"X", ((Point)gp).getPosition().getX()});
                propertiesList.add(new Object[]{"Y", ((Point)gp).getPosition().getY()});
            } else if (gp instanceof Line) {
                propertiesList.add(new Object[]{"X0", ((Line)gp).getStartPoint().getX()});
                propertiesList.add(new Object[]{"Y0", ((Line)gp).getStartPoint().getY()});
                propertiesList.add(new Object[]{"X1", ((Line)gp).getEndPoint().getX()});
                propertiesList.add(new Object[]{"Y1", ((Line)gp).getEndPoint().getY()});
                propertiesList.add(new Object[]{"LARGO", ((Line)gp).getLength()});
            } else if (gp instanceof RectangleOld) {
                propertiesList.add(new Object[]{"X", ((RectangleOld)gp).getUpperLeft().getX()});
                propertiesList.add(new Object[]{"Y", ((RectangleOld)gp).getUpperLeft().getY()});
                propertiesList.add(new Object[]{"ANCHO", ((RectangleOld)gp).getWidth()});
                propertiesList.add(new Object[]{"ALTO", ((RectangleOld)gp).getHeight()});
            } else if (gp instanceof PolyLine) {
                for (HandlePoint hp : gp.getHandlePoints()) {
                    propertiesList.add(new Object[]{"POINT(x)", hp.getX()});
                    propertiesList.add(new Object[]{"POINT(y)", hp.getY()});
                }
            } else if (gp instanceof Oval) {
                propertiesList.add(new Object[]{"X", ((Oval)gp).getStartPoint().getX()});
                propertiesList.add(new Object[]{"Y", ((Oval)gp).getStartPoint().getY()});
                propertiesList.add(new Object[]{"ANCHO", ((Oval)gp).getControlPoint().getX()});
                propertiesList.add(new Object[]{"ALTO", ((Oval)gp).getControlPoint().getY()});
            } else if (gp instanceof Curve) {

            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        rowIndex -= COMMON_PROPERTIES_COUNT;

        if (gp instanceof Point) {
            switch (rowIndex) {
                case 0: ((Point)gp).getPosition().setX(Integer.parseInt((String)aValue)); break;
                case 1: ((Point)gp).getPosition().setY(Integer.parseInt((String)aValue)); break;
                default:;
            }
        } else if (gp instanceof Line) {
            switch (rowIndex) {
                case 0: ((Line)gp).getStartPoint().setX(Integer.parseInt((String)aValue)); break;
                case 1: ((Line)gp).getStartPoint().setY(Integer.parseInt((String)aValue)); break;
                case 2: ((Line)gp).getEndPoint().setX(Integer.parseInt((String)aValue)); break;
                case 3: ((Line)gp).getEndPoint().setY(Integer.parseInt((String)aValue)); break;
                default:;
            }
        } else if (gp instanceof RectangleOld) {
            switch (rowIndex) {
                case 1: break;
                case 2: break;
                case 3: break;
                case 4: break;
                default:;
            }
        } else if (gp instanceof PolyLine) {
            switch (rowIndex) {
                case 1: break;
                case 2: break;
                case 3: break;
                case 4: break;
                default:;
            }
        } else if (gp instanceof Oval) {
            switch (rowIndex) {
                case 1: break;
                case 2: break;
                case 3: break;
                case 4: break;
                default:;
            }
        } else if (gp instanceof Curve) {
            switch (rowIndex) {
                case 1: break;
                case 2: break;
                case 3: break;
                case 4: break;
                default:;
            }
        }
    }



    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
