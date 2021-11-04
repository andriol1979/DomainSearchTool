/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vutran
 */
public class WebStatsDomainObject
{
    private String sWebsiteWorth;
    private String sSeoScore;
    private String sDateRegistered;
    private String sSiteAge;
    private List<SocialActivityObject> listSocial;
    private List<OrganicKeywordsObject> listOrganic;

    /**
     * @return the sWebsiteWorth
     */
    public WebStatsDomainObject()
    {
        listSocial = new ArrayList<>();
    }
    
    public String getsWebsiteWorth()
    {
        return sWebsiteWorth;
    }

    /**
     * @param sWebsiteWorth the sWebsiteWorth to set
     */
    public void setsWebsiteWorth(String sWebsiteWorth)
    {
        this.sWebsiteWorth = sWebsiteWorth;
    }

    /**
     * @return the sSeoScore
     */
    public String getsSeoScore()
    {
        return sSeoScore;
    }

    /**
     * @param sSeoScore the sSeoScore to set
     */
    public void setsSeoScore(String sSeoScore)
    {
        this.sSeoScore = sSeoScore;
    }

    /**
     * @return the sDateRegistered
     */
    public String getsDateRegistered()
    {
        return sDateRegistered;
    }

    /**
     * @param sDateRegistered the sDateRegistered to set
     */
    public void setsDateRegistered(String sDateRegistered)
    {
        this.sDateRegistered = sDateRegistered;
    }

    /**
     * @return the sSiteAge
     */
    public String getsSiteAge()
    {
        return sSiteAge;
    }

    /**
     * @param sSiteAge the sSiteAge to set
     */
    public void setsSiteAge(String sSiteAge)
    {
        this.sSiteAge = sSiteAge;
    }

    /**
     * @return the listSocial
     */
    public List<SocialActivityObject> getListSocial()
    {
        return listSocial;
    }

    /**
     * @return the listOrganic
     */
    public List<OrganicKeywordsObject> getListOrganic()
    {
        return listOrganic;
    }

    /**
     * @param listOrganic the listOrganic to set
     */
    public void setListOrganic(List<OrganicKeywordsObject> listOrganic)
    {
        this.listOrganic = listOrganic;
    }
    
    public void AddListSocial(SocialActivityObject oSocial)
    {
        listSocial.add(oSocial);
    }
}
