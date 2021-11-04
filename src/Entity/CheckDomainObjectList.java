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
public class CheckDomainObjectList
{
    private List<CheckDomainObject> listCheckData;
    public CheckDomainObjectList()
    {
        listCheckData = new ArrayList<>();
    }
    public void Add(CheckDomainObject oCheckData)
    {
        listCheckData.add(oCheckData);
    }
    public List<CheckDomainObject> GetListCheckDomainObject()
    {
        return listCheckData;
    }
}
