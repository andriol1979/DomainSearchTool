/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.awt.Color;

/**
 *
 * @author vutran
 */
public class ColorAssortionObject
{
    public final static int GOOGLE_ID = 1;
    public final static int BACKLINK_ID = 2;
    public final static int DMOZ_ID = 3;
    public final static String BACKLINK_MAX = "MAX";
    private int iColumnId;
    private String sColumnName;
    private int iFromValue;
    private int iToValue;
    private Object oValue;
    private Color oColor;

    /**
     * @return the iColumnId
     */
    public int getiColumnId()
    {
        return iColumnId;
    }

    /**
     * @param iColumnId the iColumnId to set
     */
    public void setiColumnId(int iColumnId)
    {
        this.iColumnId = iColumnId;
    }

    /**
     * @return the sColumnName
     */
    public String getsColumnName()
    {
        return sColumnName;
    }

    /**
     * @param sColumnName the sColumnName to set
     */
    public void setsColumnName(String sColumnName)
    {
        this.sColumnName = sColumnName;
    }

    /**
     * @return the iFromValue
     */
    public int getiFromValue()
    {
        return iFromValue;
    }

    /**
     * @param iFromValue the iFromValue to set
     */
    public void setiFromValue(int iFromValue)
    {
        this.iFromValue = iFromValue;
    }

    /**
     * @return the iToValue
     */
    public int getiToValue()
    {
        return iToValue;
    }

    /**
     * @param iToValue the iToValue to set
     */
    public void setiToValue(int iToValue)
    {
        this.iToValue = iToValue;
    }

    /**
     * @return the oColor
     */
    public Color getoColor()
    {
        return oColor;
    }

    /**
     * @param oColor the oColor to set
     */
    public void setoColor(Color oColor)
    {
        this.oColor = oColor;
    }

    /**
     * @return the oValue
     */
    public Object getoValue()
    {
        return oValue;
    }

    /**
     * @param oValue the oValue to set
     */
    public void setoValue(Object oValue)
    {
        this.oValue = oValue;
    }
}
