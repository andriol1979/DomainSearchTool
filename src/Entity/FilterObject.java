/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: FilterObject.java
 * Ngày tạo: Jan 23, 2013, 9:13:27 AM
 * Ngày sửa: Jan 23, 2013, 9:13:27 AM
 * Diễn giải:
 * Copyright 2010
 */
package Entity;

public class FilterObject
{
    private String sSearchBegin;
    private String sSearchContain;
    private String sSearchEndWith;
    private CboPriceObject oMinPrice;
    private CboPriceObject oMaxPrice;
    private boolean isDmoz;
    private boolean isAlexaRanking;
    private boolean isNoFake;
    private boolean isNoNumber;
    private boolean isNoCharacter;
    private boolean isNoHyphen;
    private String sCharacter;
    private String sNumber;
    private String sPageRank;
    private String sBackLinks;
    private String sMinAge;
    private boolean[] arrTLDs = new boolean[16];

    /**
     * @return the sSearchBegin
     */
    public String getsSearchBegin()
    {
        return sSearchBegin;
    }

    /**
     * @param sSearchBegin the sSearchBegin to set
     */
    public void setsSearchBegin(String sSearchBegin)
    {
        this.sSearchBegin = sSearchBegin;
    }

    /**
     * @return the sSearchContain
     */
    public String getsSearchContain()
    {
        return sSearchContain;
    }

    /**
     * @param sSearchContain the sSearchContain to set
     */
    public void setsSearchContain(String sSearchContain)
    {
        this.sSearchContain = sSearchContain;
    }

    /**
     * @return the sSearchEndWith
     */
    public String getsSearchEndWith()
    {
        return sSearchEndWith;
    }

    /**
     * @param sSearchEndWith the sSearchEndWith to set
     */
    public void setsSearchEndWith(String sSearchEndWith)
    {
        this.sSearchEndWith = sSearchEndWith;
    }

    /**
     * @return the oMinPrice
     */
    public CboPriceObject getoMinPrice()
    {
        return oMinPrice;
    }

    /**
     * @param oMinPrice the oMinPrice to set
     */
    public void setoMinPrice(CboPriceObject oMinPrice)
    {
        this.oMinPrice = oMinPrice;
    }

    /**
     * @return the oMaxPrice
     */
    public CboPriceObject getoMaxPrice()
    {
        return oMaxPrice;
    }

    /**
     * @param oMaxPrice the oMaxPrice to set
     */
    public void setoMaxPrice(CboPriceObject oMaxPrice)
    {
        this.oMaxPrice = oMaxPrice;
    }

    /**
     * @return the isDmoz
     */
    public boolean isIsDmoz()
    {
        return isDmoz;
    }

    /**
     * @param isDmoz the isDmoz to set
     */
    public void setIsDmoz(boolean isDmoz)
    {
        this.isDmoz = isDmoz;
    }

    /**
     * @return the isAlexaRanking
     */
    public boolean isIsAlexaRanking()
    {
        return isAlexaRanking;
    }

    /**
     * @param isAlexaRanking the isAlexaRanking to set
     */
    public void setIsAlexaRanking(boolean isAlexaRanking)
    {
        this.isAlexaRanking = isAlexaRanking;
    }

    /**
     * @return the isNoFake
     */
    public boolean isIsNoFake()
    {
        return isNoFake;
    }

    /**
     * @param isNoFake the isNoFake to set
     */
    public void setIsNoFake(boolean isNoFake)
    {
        this.isNoFake = isNoFake;
    }

    /**
     * @return the isNoNumber
     */
    public boolean isIsNoNumber()
    {
        return isNoNumber;
    }

    /**
     * @param isNoNumber the isNoNumber to set
     */
    public void setIsNoNumber(boolean isNoNumber)
    {
        this.isNoNumber = isNoNumber;
    }

    /**
     * @return the isNoCharacter
     */
    public boolean isIsNoCharacter()
    {
        return isNoCharacter;
    }

    /**
     * @param isNoCharacter the isNoCharacter to set
     */
    public void setIsNoCharacter(boolean isNoCharacter)
    {
        this.isNoCharacter = isNoCharacter;
    }

    /**
     * @return the isNoHyphen
     */
    public boolean isIsNoHyphen()
    {
        return isNoHyphen;
    }

    /**
     * @param isNoHyphen the isNoHyphen to set
     */
    public void setIsNoHyphen(boolean isNoHyphen)
    {
        this.isNoHyphen = isNoHyphen;
    }

    /**
     * @return the sCharacter
     */
    public String getsCharacter()
    {
        return sCharacter;
    }

    /**
     * @param sCharacter the sCharacter to set
     */
    public void setsCharacter(String sCharacter)
    {
        this.sCharacter = sCharacter;
    }

    /**
     * @return the sPageRank
     */
    public String getsPageRank()
    {
        return sPageRank;
    }

    /**
     * @param sPageRank the sPageRank to set
     */
    public void setsPageRank(String sPageRank)
    {
        this.sPageRank = sPageRank;
    }

    /**
     * @return the sBackLinks
     */
    public String getsBackLinks()
    {
        return sBackLinks;
    }

    /**
     * @param sBackLinks the sBackLinks to set
     */
    public void setsBackLinks(String sBackLinks)
    {
        this.sBackLinks = sBackLinks;
    }

    /**
     * @return the sMinAge
     */
    public String getsMinAge()
    {
        return sMinAge;
    }

    /**
     * @param sMinAge the sMinAge to set
     */
    public void setsMinAge(String sMinAge)
    {
        this.sMinAge = sMinAge;
    }

    /**
     * @return the arrTLDs
     */
    public boolean[] getArrTLDs()
    {
        return arrTLDs;
    }

    /**
     * @param arrTLDs the arrTLDs to set
     */
    public void setArrTLDs(boolean[] arrTLDs)
    {
        this.arrTLDs = arrTLDs;
    }

    /**
     * @return the sNumber
     */
    public String getsNumber()
    {
        return sNumber;
    }

    /**
     * @param sNumber the sNumber to set
     */
    public void setsNumber(String sNumber)
    {
        this.sNumber = sNumber;
    }
}
