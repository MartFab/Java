package gui.panels;

import graphics.primitives.GraphicPrimitive;
import java.util.ArrayList;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class GraphicPrimitivesTableModel extends AbstractTableModel {

    private List<GraphicPrimitive> graphicPrimitives = new ArrayList<GraphicPrimitive>();

    private String [] columnNames = new String[] {"Primitiva"};

    public GraphicPrimitivesTableModel() {
        super();
    }

    public int getRowCount() {
        return getGraphicPrimitives().size();
    }

    public int getColumnCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return graphicPrimitives.get(rowIndex);
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

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }


}
