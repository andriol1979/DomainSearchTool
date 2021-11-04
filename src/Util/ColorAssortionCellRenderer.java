/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author vutran
 */
public class ColorAssortionCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        Color oColor = (Color) value;
        setBackground(oColor);
        setText("");
        return c;
    }
}
