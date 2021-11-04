/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

/**
 *
 * @author vutran
 */
public class LogException extends Thread
{
    private PipedInputStream pis;
    private String sLogFileName;

    public LogException(String sLogFileName) throws IOException
    {
        this.sLogFileName = sLogFileName;
        pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream(pis);  // throws IOException
        PrintStream ps = new PrintStream(pos);
//        System.setOut(ps);
        System.setErr(ps);
    }

    @Override
    public void run()
    {
        try
        {
            byte[] buf = new byte[Byte.MAX_VALUE];
            int bytesRead = 0;
            while ((bytesRead = pis.read(buf)) != -1)
            {
                WriteLogFile.Write(sLogFileName, new String(buf, 0, bytesRead));
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            System.err.println("Fatal Error:  Exiting reader.");
        }
        finally
        {
            try
            {
                pis.close();
            }
            catch (Exception e)
            {
            }
        }
    }
}

class WriteLogFile
{
    public static void Write(String sFileName, String sMessage) throws IOException
    {
        try (FileWriter fw = new FileWriter(sFileName, true))
        {
            fw.write(sMessage);
        }
    }
}
