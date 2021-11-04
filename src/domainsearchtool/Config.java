/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domainsearchtool;

import Util.UtilClass;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author huynguyen
 */
public class Config
{
    private final String _sSourcePath = URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
    private final String _sConfigPath = UtilClass.GetHomeDir(_sSourcePath) + UtilClass.ConvertSystemSeparator("/src/data/config/");
    private final String _sOutputPath = UtilClass.GetHomeDir(_sSourcePath) + UtilClass.ConvertSystemSeparator("/src/data/output/");
    private final String _sLogPath = UtilClass.GetHomeDir(_sSourcePath) + UtilClass.ConvertSystemSeparator("/log/");
    private final String sFilterConfigFilePath = "FilterConfig.xml";
    private final String sColorAssortConfigFilePath = "ColorAssortConfig.xml";
    private final String sCheckDomainFilePath = "CheckDomain.xml";
    private final String _sExcelFile = ".xls";
    private final String _sCsvFile = ".csv";
    private SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");
    
    public String GetConfigPath()
    {
        return _sConfigPath;
    }

    public String GetFilterConfigFileName()
    {
        return sFilterConfigFilePath;
    }
    public String GetColorAssortConfigFileName()
    {
        return sColorAssortConfigFilePath;
    }
    public String GetOutputPath()
    {
        return _sOutputPath;
    }
    public String GetLogPath()
    {
        return _sLogPath;
    }
    public String GetExcelFile()
    {
        return _sExcelFile;
    }

    public String GetCsvFile()
    {
        return _sCsvFile;
    }
    
    public String GetCheckDomainFileName()
    {
        return sCheckDomainFilePath;
    }
    public String GetLogFileName()
    {
        return df.format(Calendar.getInstance().getTime()) + ".log";
    }
}
