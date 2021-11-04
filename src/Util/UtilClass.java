/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author huynguyen
 */
public class UtilClass
{
    public static final String TOKEN_SYMBOL = "|";
    public static String GetHomeDir(String SourcePath)
    {
        //return System.getProperty("user.dir");
        String Temp = "";
        if (IsWindowsOS())
        {
            Temp = SourcePath.trim().substring(1);
        }
        else
        {
            Temp = SourcePath;
        }
        String Result = "";
        int TempLength = Temp.length();
        if (Temp.toLowerCase().endsWith("/build/classes/"))
        {
            Result = Temp.substring(0, TempLength - 15);
        }
        else
        {
            Result = Temp.substring(0, Temp.lastIndexOf("/"));
        }
        return Result.replace("/", GetSystemSeparator());
    }

    public static boolean IsWindowsOS()
    {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows"))
        {
            return true;
        }
        return false;
    }

    public static String GetSystemSeparator()
    {
        return System.getProperty("file.separator");
    }

    public static String ConvertSystemSeparator(String PathFile)
    {
        String Separator = System.getProperty("file.separator");
        //System.out.println(System.getProperty("file.separator"));
        String NewPathFile = PathFile.replace("|", Separator);
        //System.out.println(NewPathFile);
        return NewPathFile;
    }
}