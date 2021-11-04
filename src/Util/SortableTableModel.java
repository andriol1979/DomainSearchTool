/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vutran
 */
public class SortableTableModel extends DefaultTableModel
{
    int[] indexes;
    TableSorter sorter;

    public SortableTableModel()
    {
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        int rowIndex = row;
        if (indexes != null)
        {
            try
            {
                rowIndex = indexes[row];
            }
            catch(ArrayIndexOutOfBoundsException ex){}
        }
        return super.getValueAt(rowIndex, col);
    }

    @Override
    public void setValueAt(Object value, int row, int col)
    {
        int rowIndex = row;
        if (indexes != null)
        {
            rowIndex = indexes[row];
        }
        super.setValueAt(value, rowIndex, col);
    }

    public void sortByColumn(int column, boolean isAscent)
    {
        if (sorter == null)
        {
            sorter = new TableSorter(this);
        }
        sorter.sort(column, isAscent);
        fireTableDataChanged();
    }

    public int[] getIndexes()
    {
        int n = getRowCount();
        if (indexes != null)
        {
            if (indexes.length == n)
            {
                return indexes;
            }
        }
        indexes = new int[n];
        for (int i = 0; i < n; i++)
        {
            indexes[i] = i;
        }
        return indexes;
    }
}