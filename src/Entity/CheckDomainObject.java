/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author vutran
 */
public class CheckDomainObject
{
    public final static String FAKE_YES = "Yes";
    public final static String FAKE_NO = "No";
    public final static String DMOZ_YES = "YES";
    public final static String DMOZ_NO = "NO";
//    public final static String AGE_EMPTY = "-";
    private String sDomainName;
    private int iGooglePagerank;
    private boolean isFakePagerank;
    private int iBacklinks;
//    private String sAge;
    private DomainAgeObject oDoMainAge;
    private int iAlexaPagerank;
    private boolean isDmoz;

    /**
     * @return the iGooglePagerank
     */
    public int getiGooglePagerank()
    {
        return iGooglePagerank;
    }

    /**
     * @param iGooglePagerank the iGooglePagerank to set
     */
    public void setiGooglePagerank(int iGooglePagerank)
    {
        this.iGooglePagerank = iGooglePagerank;
    }

    /**
     * @return the isFakePagerank
     */
    public boolean isIsFakePagerank()
    {
        return isFakePagerank;
    }

    /**
     * @param isFakePagerank the isFakePagerank to set
     */
    public void setIsFakePagerank(boolean isFakePagerank)
    {
        this.isFakePagerank = isFakePagerank;
    }

    /**
     * @return the iBacklinks
     */
    public int getiBacklinks()
    {
        return iBacklinks;
    }

    /**
     * @param iBacklinks the iBacklinks to set
     */
    public void setiBacklinks(int iBacklinks)
    {
        this.iBacklinks = iBacklinks;
    }

    /**
     * @return the iAlexaPagerank
     */
    public int getiAlexaPagerank()
    {
        return iAlexaPagerank;
    }

    /**
     * @param iAlexaPagerank the iAlexaPagerank to set
     */
    public void setiAlexaPagerank(int iAlexaPagerank)
    {
        this.iAlexaPagerank = iAlexaPagerank;
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
