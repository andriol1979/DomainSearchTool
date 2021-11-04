/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: CboPriceObject.java
 * Ngày tạo: Jan 24, 2013, 10:40:43 AM
 * Ngày sửa: Jan 24, 2013, 10:40:43 AM
 * Diễn giải:
 * Copyright 2010
 */
package Entity;

public class CboPriceObject
{
    private String sDisplay;
    private int iValue;

    //Constructor 1
    public CboPriceObject()
    {
    }
    //Constructor 2
    public CboPriceObject(String sDisplay, int iValue)
    {
        this.sDisplay = sDisplay;
        this.iValue = iValue;
    }
    /**
     * @return the sDisplay
     */
    public String getsDisplay()
    {
        return sDisplay;
    }

    /**
     * @param sDisplay the sDisplay to set
     */
    public void setsDisplay(String sDisplay)
    {
        this.sDisplay = sDisplay;
    }

    /**
     * @return the iValue
     */
    public int getiValue()
    {
        return iValue;
    }

    /**
     * @param iValue the iValue to set
     */
    public void setiValue(int iValue)
    {
        this.iValue = iValue;
    }
    //override
    @Override
    public String toString()
    {
        return sDisplay;
    }
}
