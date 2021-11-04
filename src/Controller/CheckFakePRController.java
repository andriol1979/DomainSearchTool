/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import domainsearchtool.Context;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vutran
 */
public class CheckFakePRController
{
    private final Context _context;
    private static CheckFakePRController instance;

    private CheckFakePRController(Context context)
    {
        _context = context;
    }

    public static CheckFakePRController Instance(Context context)
    {
        if (instance == null)
        {
            instance = new CheckFakePRController(context);
        }
        return instance;
    }

    public boolean GetFakePRObject(final String sDomainName) throws InterruptedException, ExecutionException
    {
        String sFullUrl = "https://www.google.com/search?hl=en&q=info:{0}&oq=info:{0}";
        MessageFormat format = new MessageFormat(sFullUrl);
        Object[] arrParam = {sDomainName};
        final String sUrl = format.format(arrParam);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Boolean> callable = new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                try
                {
                    String sItemContent = _context.GetHttp().DoGetFakePRGoogle(sUrl);
//                    Util.File.CreateFile("checkfake.html", sItemContent);
                    Pattern patternItem = Pattern.compile("no information is available for the URL", Pattern.DOTALL);
                    Matcher matcherItem = patternItem.matcher(sItemContent);
                    return matcherItem.find();
                }
                catch (IOException ex)
                {
                    throw ex;
                }
            }
        };
        Future<Boolean> future = executor.submit(callable);
        executor.shutdown();
        return future.get();
    }
}
