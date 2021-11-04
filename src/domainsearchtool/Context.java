/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domainsearchtool;

import Util.Http;

/**
 *
 * @author huynguyen
 */
public class Context
{
    private final String SLogFile = "Logs.dat";
    private final static Object OInstanceLocker = new Object();
    private static Context _instance;
    private final Http _http;
    private final Config _config;

    public static Context Instance()
    {
        synchronized (OInstanceLocker)
        {
            if (_instance == null)
            {
                _instance = new Context();
            }
        }
        return _instance;
    }

    private Context()
    {
        _http = new Http();
        _config = new Config();
    }

    public Http GetHttp()
    {
        return _http;
    }

    public Config GetConfig()
    {
        return _config;
    }
}
