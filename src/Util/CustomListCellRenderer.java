/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: CustomListCellRenderer.java
 * Ngày tạo: Jan 24, 2013, 10:52:21 AM
 * Ngày sửa: Jan 24, 2013, 10:52:21 AM
 * Diễn giải:
 * Copyright 2010
 */
package Util;

import javax.swing.DefaultListCellRenderer;
import javax.swing.SwingConstants;

public class CustomListCellRenderer extends DefaultListCellRenderer
{
    public static CustomListCellRenderer PositionRenderer(int Position)
    {
        CustomListCellRenderer renderer = new CustomListCellRenderer();
        switch(Position)
        {
            case 0://LEFT
            {
                renderer.setHorizontalAlignment(SwingConstants.LEFT);
                break;
            }
            case 1://CENTER
            {
                renderer.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            }
            case 2://RIGHT
            {
                renderer.setHorizontalAlignment(SwingConstants.RIGHT);
                break;
            }
            case 3://TRAILING
            {
                renderer.setHorizontalAlignment(SwingConstants.TRAILING);
                break;
            }
        }
        return renderer;
    }
}
