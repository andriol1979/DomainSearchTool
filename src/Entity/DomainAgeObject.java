/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author vutran
 */
public class DomainAgeObject
{
    private String sDomainAge;
    private int iDomainAge;

    /**
     * @return the sDomainAge
     */
    public String getsDomainAge()
    {
        return sDomainAge;
    }

    /**
     * @param sDomainAge the sDomainAge to set
     */
    public void setsDomainAge(String sDomainAge)
    {
        this.sDomainAge = sDomainAge;
    }

    /**
     * @return the iDomainAge
     */
    public int getiDomainAge()
    {
        return iDomainAge;
    }

    /**
     * @param iDomainAge the iDomainAge to set
     */
    public void setiDomainAge(int iDomainAge)
    {
        this.iDomainAge = iDomainAge;
    }
    
    @Override
    public String toString()
    {
        return sDomainAge;
    }
}
