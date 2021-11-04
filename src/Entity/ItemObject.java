/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.net.URI;

/**
 *
 * @author vutran
 */
public class ItemObject
{
    private String sDomainName;
    private int iGooglePR;
    private String sFakePR;
    private int iBacklink;
    private int iGoogleIndex;
//    private String sAge;
    private DomainAgeObject oDoMainAge;
    private int iAlexa;
    private String sDmoz;
    private int iPrice;
    private String sAuctionType;
    private String sAuctionEnd;
    private URI uriDomainLink;

    /**
     * @return the sDomainName
     */
    public String getsDomainName()
    {
        return sDomainName;
    }

    /**
     * @param sDomainName the sDomainName to set
     */
    public void setsDomainName(String sDomainName)
    {
        this.sDomainName = sDomainName;
    }

    /**
     * @return the iGooglePR
     */
    public int getiGooglePR()
    {
        return iGooglePR;
    }

    /**
     * @param iGooglePR the iGooglePR to set
     */
    public void setiGooglePR(int iGooglePR)
    {
        this.iGooglePR = iGooglePR;
    }

    /**
     * @return the sFakePR
     */
    public String getsFakePR()
    {
        return sFakePR;
    }

    /**
     * @param sFakePR the sFakePR to set
     */
    public void setsFakePR(String sFakePR)
    {
        this.sFakePR = sFakePR;
    }

    /**
     * @return the iGoogleIndex
     */
    public int getiGoogleIndex()
    {
        return iGoogleIndex;
    }

    /**
     * @param iGoogleIndex the iGoogleIndex to set
     */
    public void setiGoogleIndex(int iGoogleIndex)
    {
        this.iGoogleIndex = iGoogleIndex;
    }

    /**
     * @return the iAlexa
     */
    public int getiAlexa()
    {
        return iAlexa;
    }

    /**
     * @param iAlexa the iAlexa to set
     */
    public void setiAlexa(int iAlexa)
    {
        this.iAlexa = iAlexa;
    }

    /**
     * @return the sDmoz
     */
    public String getsDmoz()
    {
        return sDmoz;
    }

    /**
     * @param sDmoz the sDmoz to set
     */
    public void setsDmoz(String sDmoz)
    {
        this.sDmoz = sDmoz;
    }

    /**
     * @return the iPrice
     */
    public int getiPrice()
    {
        return iPrice;
    }

    /**
     * @param iPrice the iPrice to set
     */
    public void setiPrice(int iPrice)
    {
        this.iPrice = iPrice;
    }

    /**
     * @return the sAuctionType
     */
    public String getsAuctionType()
    {
        return sAuctionType;
    }

    /**
     * @param sAuctionType the sAuctionType to set
     */
    public void setsAuctionType(String sAuctionType)
    {
        this.sAuctionType = sAuctionType;
    }

    /**
     * @return the sAuctionEnd
     */
    public String getsAuctionEnd()
    {
        return sAuctionEnd;
    }

    /**
     * @param sAuctionEnd the sAuctionEnd to set
     */
    public void setsAuctionEnd(String sAuctionEnd)
    {
        this.sAuctionEnd = sAuctionEnd;
    }

    /**
     * @return the uriDomainLink
     */
    public URI getUriDomainLink()
    {
        return uriDomainLink;
    }

    /**
     * @param uriDomainLink the uriDomainLink to set
     */
    public void setUriDomainLink(URI uriDomainLink)
    {
        this.uriDomainLink = uriDomainLink;
    }

    /**
     * @return the iBacklink
     */
    public int getiBacklink()
    {
        return iBacklink;
    }

    /**
     * @param iBacklink the iBacklink to set
     */
    public void setiBacklink(int iBacklink)
    {
        this.iBacklink = iBacklink;
    }

    /**
     * @return the oDoMainAge
     */
    public DomainAgeObject getoDoMainAge()
    {
        return oDoMainAge;
    }

    /**
     * @param oDoMainAge the oDoMainAge to set
     */
    public void setoDoMainAge(DomainAgeObject oDoMainAge)
    {
        this.oDoMainAge = oDoMainAge;
    }
}
