/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: Csv.java
 * Ngày tạo: Nov 27, 2012, 2:28:40 PM
 * Ngày sửa: Nov 27, 2012, 2:28:40 PM
 * Diễn giải:
 * Copyright 2010
 */
package Util;
import Entity.ItemObject;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Csv
{
    public static void WriteCsvFile(String sFileName, List<ItemObject> lstItems) throws IOException
    {
        CSVWriter writer = new CSVWriter(new FileWriter(sFileName), '\t',
                '\"', CSVWriter.NO_ESCAPE_CHARACTER, "\n");
        for(ItemObject oItem : lstItems)
        {
            String[] entries = {oItem.getsDomainName(), String.valueOf(oItem.getiGooglePR()), oItem.getsFakePR(),
                String.valueOf(oItem.getiBacklink()), String.valueOf(oItem.getiGoogleIndex()), oItem.getoDoMainAge().getsDomainAge(), String.valueOf(oItem.getiAlexa()),
                oItem.getsDmoz(), String.valueOf(oItem.getiPrice()), oItem.getsAuctionType(), oItem.getsAuctionEnd()};
            writer.writeNext(entries);
        }
        writer.close();
    }
}
