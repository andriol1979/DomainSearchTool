/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Entity.ColorAssortionObject;
import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author vutran
 */
public class DmozCellRenderer extends DefaultTableCellRenderer
{
    private List<ColorAssortionObject> list;
    public DmozCellRenderer(List<ColorAssortionObject> list)
    {
        this.list = list;
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        setBackground(table.getBackground());
        if(list == null)
        {
            return c;
        }
        for(ColorAssortionObject oColorAssort : list)
        {
            if(oColorAssort.getiColumnId() == ColorAssortionObject.DMOZ_ID)
            {
                try
                {
                    if(oColorAssort.getoValue().toString().equals(value.toString()))
                    {
                        setBackground(oColorAssort.getoColor());
                        break;
                    }
                }
                catch(NullPointerException ex){}
            }
        }
        if(isSelected)
        {
            setBackground(table.getSelectionBackground());
        }
//        setText(value.toString());
        return c;
    }
    @Override
    public void setValue(Object value)
    {
        //  Format the Object before setting its value in the renderer
        super.setValue(value);
    }
}
