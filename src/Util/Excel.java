/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: Excel.java
 * Ngày tạo: Nov 23, 2012, 5:05:51 PM
 * Ngày sửa: Nov 23, 2012, 5:05:51 PM
 * Diễn giải:
 * Copyright 2010
 */
package Util;

import Entity.ItemObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Excel
{
    public static void WriteExcelFile(String sFileName, List<ItemObject> lstItems) throws IOException, WriteException
    {
        if (lstItems == null || lstItems.isEmpty())
        {
            return;
        }
        WorkbookSettings wSetting = new WorkbookSettings();
        wSetting.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(new java.io.File(sFileName), wSetting);
        WritableSheet wSheet = workbook.createSheet("Item Object", 0);
        wSheet.addCell(new Label(0, 0, "Sr.No"));
        wSheet.addCell(new Label(1, 0, "Domain"));
        wSheet.addCell(new Label(2, 0, "Google PR"));
        wSheet.addCell(new Label(3, 0, "Fake PR"));
        wSheet.addCell(new Label(4, 0, "Backlinks"));
        wSheet.addCell(new Label(5, 0, "Goole Index"));
        wSheet.addCell(new Label(6, 0, "Age"));
        wSheet.addCell(new Label(7, 0, "Alexa"));
        wSheet.addCell(new Label(8, 0, "Dmoz"));
        wSheet.addCell(new Label(9, 0, "Price (USD)"));
        wSheet.addCell(new Label(10, 0, "Auction Type"));
        wSheet.addCell(new Label(11, 0, "Auction End"));

        int row = 1;
        for (int i = 0; i < lstItems.size(); i++)
        {
            ItemObject oItem = lstItems.get(i);
            wSheet.addCell(new Label(0, row, String.valueOf(row)));
            wSheet.addCell(new Label(1, row, oItem.getsDomainName()));
            wSheet.addCell(new Label(2, row, String.valueOf(oItem.getiGooglePR())));
            wSheet.addCell(new Label(3, row, oItem.getsFakePR()));
            wSheet.addCell(new Label(4, row, String.valueOf(oItem.getiBacklink())));
            wSheet.addCell(new Label(5, row, String.valueOf(oItem.getiGoogleIndex())));
            wSheet.addCell(new Label(6, row, oItem.getoDoMainAge().getsDomainAge()));
            wSheet.addCell(new Label(7, row, String.valueOf(oItem.getiAlexa())));
            wSheet.addCell(new Label(8, row, oItem.getsDmoz()));
            wSheet.addCell(new Label(9, row, String.valueOf(oItem.getiPrice())));
            wSheet.addCell(new Label(10, row, oItem.getsAuctionType()));
            wSheet.addCell(new Label(11, row, oItem.getsAuctionEnd()));
            row++;
        }
        workbook.write();
        workbook.close();
    }
}
