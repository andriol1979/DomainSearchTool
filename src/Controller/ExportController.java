/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.ItemObject;
import Util.Csv;
import Util.Excel;
import domainsearchtool.Context;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import jxl.write.WriteException;

/**
 *
 * @author vutran
 */
public class ExportController
{
    public static boolean ExportResultToFile(Context context, Frame fMain, File f, List<ItemObject> lstItems) throws IOException, WriteException
    {
        //Check file extension
        if(f.exists())
        {
            int result = JOptionPane.showConfirmDialog(fMain, "File is exists, do you want to override?", "Exists comfirm!!!", JOptionPane.YES_NO_OPTION);
            if (result == 1)
            {
                return false;
            }
        }
        System.out.println(f.getPath());
        String extension = "." + f.getName().replaceAll("[^\\.].*\\.([^\\.]+)$","$1");
        if(context.GetConfig().GetExcelFile().equalsIgnoreCase(extension))
        {
            //Write excel file
            Excel.WriteExcelFile(f.getPath(), lstItems);
        }
        else if(context.GetConfig().GetCsvFile().equalsIgnoreCase(extension))
        {
            //Write csv file
            Csv.WriteCsvFile(f.getPath(), lstItems);
        }
        else
        {
            JOptionPane.showMessageDialog(fMain, "Extension file is not invalid, [xls] and [csv] only!");
            return false;
        }
        return true;
    }
}
