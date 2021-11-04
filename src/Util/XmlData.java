/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: clsXmlData.java
 * Ngày tạo: Nov 22, 2012, 1:53:31 PM
 * Ngày sửa: Nov 22, 2012, 1:53:31 PM
 * Diễn giải:
 * Copyright 2010
 */
package Util;

import Entity.CheckDomainObject;
import Entity.CheckDomainObjectList;
import Entity.ColorAssortionObject;
import Entity.ColorAssortionObjectList;
import Entity.FilterObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import java.util.List;

public class XmlData
{
    public static void SaveFilterConfigToXml(FilterObject oFilter, String sFileName) throws IOException
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("filterconfig", FilterObject.class);
        String sXml = xstream.toXML(oFilter);
//        System.out.println(sXml);
        File.WriteFile(sFileName, sXml);
    }

    public static FilterObject LoadFilterConfigFromXml(String sFileName) throws IOException
    {
        String sXml = File.ReadFile(sFileName);
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("filterconfig", FilterObject.class);
        return (FilterObject) xstream.fromXML(sXml);
    }

    public static void SaveListCheckDomainObjectToXml(List<CheckDomainObject> listCheckData, String sFileName) throws IOException
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("checkdomainobject", CheckDomainObject.class);
        xstream.alias("checkdomainobjectlist", CheckDomainObjectList.class);
        xstream.addImplicitCollection(CheckDomainObjectList.class, "listCheckData");
        CheckDomainObjectList list = new CheckDomainObjectList();
        for (CheckDomainObject oCheckData : listCheckData)
        {
            list.Add(oCheckData);
        }
        String sXml = xstream.toXML(list);
        File.WriteFile(sFileName, sXml);
    }

    public static List<CheckDomainObject> LoadListCheckDomainObjectFromXml(String sFileName) throws IOException
    {
        String sXml = File.ReadFile(sFileName);
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("checkdomainobject", CheckDomainObject.class);
        xstream.alias("checkdomainobjectlist", CheckDomainObjectList.class);
        xstream.addImplicitCollection(CheckDomainObjectList.class, "listCheckData");
        CheckDomainObjectList cList = (CheckDomainObjectList) xstream.fromXML(sXml);
        return cList.GetListCheckDomainObject();
    }
    
    public static void SaveListColorAssortObjectToXml(List<ColorAssortionObject> listColorAssort, String sFileName) throws IOException
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("colorassortionobject", ColorAssortionObject.class);
        xstream.alias("colorassortionobjectlist", ColorAssortionObjectList.class);
        xstream.addImplicitCollection(ColorAssortionObjectList.class, "listColorAssort");
        ColorAssortionObjectList list = new ColorAssortionObjectList();
        for (ColorAssortionObject oColorAssort : listColorAssort)
        {
            list.Add(oColorAssort);
        }
        String sXml = xstream.toXML(list);
        File.WriteFile(sFileName, sXml);
    }
    public static List<ColorAssortionObject> LoadListColorAssortObjectFromXml(String sFileName) throws IOException
    {
        String sXml = File.ReadFile(sFileName);
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("colorassortionobject", ColorAssortionObject.class);
        xstream.alias("colorassortionobjectlist", ColorAssortionObjectList.class);
        xstream.addImplicitCollection(ColorAssortionObjectList.class, "listColorAssort");
        ColorAssortionObjectList cList = (ColorAssortionObjectList) xstream.fromXML(sXml);
        return cList.GetListColorAssortionObject();
    }
}
