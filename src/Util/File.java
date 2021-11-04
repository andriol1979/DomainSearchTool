/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.*;

/**
 *
 * @author Huy Nguyen
 */
public class File
{
    public static void CreateFile(String sFileName, String sContent) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(sFileName));
        writer.write(sContent);
        writer.newLine();
        writer.close();
    }

    public static java.io.File WriteFile(String sFileName, String sContent) throws IOException
    {
        java.io.File f = new java.io.File(sFileName);
        FileWriter fOut = new FileWriter(f);
        BufferedWriter writer = new BufferedWriter(fOut);
        writer.write(sContent);
        writer.newLine();
        writer.close();
        return f;
    }

    public static String ReadFile(String sFileName) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(sFileName));
        String sLine = null;
        StringBuilder stringBuilder = new StringBuilder();
        String sLineSeparator = System.getProperty("line.separator");
        while ((sLine = reader.readLine()) != null)
        {
            stringBuilder.append(sLine);
            stringBuilder.append(sLineSeparator);
        }
        return stringBuilder.toString();
    }

    public static void CreateFolder(String folderPath)
    {
        java.io.File f = new java.io.File(folderPath);
        if (!f.exists())
        {
            f.mkdirs();
        }
    }

    public static boolean DeleteFile(String sFileName)
    {
        try
        {
            java.io.File f = new java.io.File(sFileName);
            return f.delete();
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static void DeleteFile_AfterNday(String sFileName, int iBackDays)
    {
        java.io.File file = new java.io.File(sFileName);
        long purgeTime = System.currentTimeMillis() - (iBackDays * 24 * 60 * 60 * 1000);
        if(file.lastModified() < purgeTime)
        {
            file.delete();
        }
    }

    public static String GetHomeDir(String sSourcePath)
    {
        //return System.getProperty("user.dir");
        String sTemp = "";
        sSourcePath = sSourcePath.replace("file:", "");
        if (IsWindowsOS())
        {
            sTemp = sSourcePath.trim().substring(1);
        }
        else
        {
            sTemp = sSourcePath;
        }
        String sResult = "";
        int buildIndex = sTemp.lastIndexOf("build");
        if (buildIndex > 0)
        {
            sResult = sTemp.substring(0, buildIndex);
        }
        else
        {
            int jarIndex = sTemp.lastIndexOf("com-tekde-linet");
            if (jarIndex > 0)
            {
                sResult = sTemp.substring(0, jarIndex);
            }
            else
            {
                sResult = sTemp;
            }
        }
        return sResult.replace("/", GetSystemSeparator());
    }

    private static boolean IsWindowsOS()
    {
        String sOs = System.getProperty("os.name");
        if (sOs.toLowerCase().contains("windows"))
        {
            return true;
        }
        return false;
    }

    public static String ConvertSystemSeparator(String sPathFile)
    {
        String sSeparator = System.getProperty("file.separator");
        //System.out.println(System.getProperty("file.separator"));
        String sNewPathFile = sPathFile.replace("|", sSeparator);
        //System.out.println(NewPathFile);
        return sNewPathFile;
    }

    private static String GetSystemSeparator()
    {
        return System.getProperty("file.separator");
    }

    File(String filePath)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
