/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class FormatNumberCellRenderer extends DefaultTableCellRenderer
{
    private Format formatter;
    public FormatNumberCellRenderer(NumberFormat formatter)
    {
        //this.locale = locale;
        this.formatter = formatter;
    }
    public FormatNumberCellRenderer(Format formatter)
    {
        //this.locale = locale;
        this.formatter = formatter;
    }
    @Override
    public void setValue(Object value)
    {
        //  Format the Object before setting its value in the renderer
        try
        {
            if (value != null)
            {
                value = formatter.format(value);
            }
        }
        catch (IllegalArgumentException e)
        {
        }
        super.setValue(value);
    }

    public static FormatNumberCellRenderer getCurrencyRenderer()
    {
        return new FormatNumberCellRenderer(NumberFormat.getCurrencyInstance());
    }
    public static FormatNumberCellRenderer getIntegerRenderer()
    {
        return new FormatNumberCellRenderer(NumberFormat.getIntegerInstance());
    }
    public static FormatNumberCellRenderer getPercentRenderer()
    {
        return new FormatNumberCellRenderer(NumberFormat.getPercentInstance());
    }
    public static FormatNumberCellRenderer getDateTimeRenderer()
    {
        return new FormatNumberCellRenderer(DateFormat.getDateTimeInstance());
    }
    public static FormatNumberCellRenderer getTimeRenderer()
    {
        return new FormatNumberCellRenderer(DateFormat.getTimeInstance());
    }
    
    //Format with Locale
    public static FormatNumberCellRenderer getCurrencyRenderer(Locale locale, int precision)
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        FormatNumberCellRenderer renderer = new FormatNumberCellRenderer(nf);
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        return renderer;
    }
    public static FormatNumberCellRenderer getIntegerRenderer(Locale locale, int precision)
    {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        FormatNumberCellRenderer renderer = new FormatNumberCellRenderer(nf);
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        return renderer;
    }
    public static FormatNumberCellRenderer getIntegerRenderer(Locale locale, int precision, Color bgColor)
    {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        FormatNumberCellRenderer renderer = new FormatNumberCellRenderer(nf);
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        renderer.setBackground(bgColor);
        return renderer;
    }
    public static FormatNumberCellRenderer getPercentRenderer(Locale locale, int precision)
    {
        NumberFormat nf = NumberFormat.getPercentInstance(locale);
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        FormatNumberCellRenderer renderer = new FormatNumberCellRenderer(nf);
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        return renderer;
    }
    //Format with String pattern
    public static FormatNumberCellRenderer getCurrencyRenderer(String pattern)
    {
        DecimalFormat f = new DecimalFormat(pattern);
        return new FormatNumberCellRenderer(f);
    }
    public static FormatNumberCellRenderer getIntegerRenderer(String pattern)
    {
        DecimalFormat f = new DecimalFormat(pattern);
        return new FormatNumberCellRenderer(f);
    }
    public static FormatNumberCellRenderer getPercentRenderer(String pattern)
    {
        DecimalFormat f = new DecimalFormat(pattern);
        return new FormatNumberCellRenderer(f);
    }
    public static FormatNumberCellRenderer getDateTimeRenderer(String pattern)
    {
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        return new FormatNumberCellRenderer(f);
    }
    public static FormatNumberCellRenderer getTimeRenderer(String pattern)
    {
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        return new FormatNumberCellRenderer(f);
    }
    /**
     * 
     * @param Position int
     * 0 - LEFT
     * 1 - CENTER
     * 2 - RIGHT
     * 3 - TRAILING
     * @return FormatNumberCellRenderer
     */
    public static FormatNumberCellRenderer PositionRenderer(int Position)
    {
        FormatNumberCellRenderer renderer = new FormatNumberCellRenderer(NumberFormat.getInstance());
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
