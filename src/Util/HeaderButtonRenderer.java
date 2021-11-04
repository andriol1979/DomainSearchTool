/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author vutran
 */
public class HeaderButtonRenderer extends JButton implements TableCellRenderer
{
    public HeaderButtonRenderer()
    {
        setMargin(new Insets(0, 0, 0, 0));
        setHorizontalTextPosition(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JButton button = this;
        button.setText((value == null) ? "" : value.toString());
        return button;
    }
}
