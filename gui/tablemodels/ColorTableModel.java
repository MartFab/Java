package gui.tablemodels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ColorTableModel implements TableModel {

    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    private Color borderColor = Color.RED;
    private Color backgroundColor = Color.BLACK;
    private Color fillColor = Color.BLUE;


  public ColorTableModel(){
    super();
  }

    public int getRowCount() {
        return 3;
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        return "";
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else {
            return Color.class;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean editable = false;
        if (columnIndex == 1) {
            editable = true;
        }
        return editable;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = "";
        if (columnIndex == 0) {
            switch (rowIndex) {
                case 0:
                    value = "Color de fondo";
                    break;
                case 1:
                    value = "Color de lapiz";
                    break;
                case 2:
                    value = "Color de relleno";
                    break;
            }
        } else {
            switch (rowIndex) {
                case 0:
                    value = backgroundColor;
                    break;
                case 1:
                    value = borderColor;
                    break;
                case 2:
                    value = fillColor;
                    break;
            }
        }
        return value;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            if (rowIndex == 0) {
                backgroundColor = (Color)aValue;
            } else {
                borderColor = (Color)aValue;
            }
        }
    }

    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

}
