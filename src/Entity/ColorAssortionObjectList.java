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
public class ColorAssortionObjectList
{
    private List<ColorAssortionObject> listColorAssort;
    public ColorAssortionObjectList()
    {
        listColorAssort = new ArrayList<>();
    }
    public void Add(ColorAssortionObject oColorAssort)
    {
        listColorAssort.add(oColorAssort);
    }
    public List<ColorAssortionObject> GetListColorAssortionObject()
    {
        return listColorAssort;
    }
}
