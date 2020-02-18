
package mintwire.res.tablemodels;

import javax.swing.table.AbstractTableModel;


public class model1 extends AbstractTableModel {
    private String[] columns = { "File name", "File size", "Received from",
        "Date & time" };

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       return 0;
    }
    
}
