/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Huy Nguyen
 */
public class Http
{
    public String CrawlData(String sUrl, String sMethod) throws IOException
    {
        String sReturn = "";
        switch (sMethod)
        {
            case "GET":
                sReturn = DoGet(sUrl);
                break;
        }
        return sReturn;
    }

    private String DoGet(String sUrl) throws IOException
    {
        URL url = new URL(sUrl);
        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())))
        {
            output = "";
            String line;
            while ((line = reader.readLine()) != null)
            {
                //System.out.println(line);
                output += line;
            }
        }
        return output;
    }

    public String DoPost(String sUrl, String sPostData) throws IOException, NullPointerException
    {
        String sCookie = GetCookie("http://www.namecatch.com/");
//        System.out.println("Cookie: " + sCookie);
        sCookie = sCookie.split(";")[0];
        sCookie = "__utma=150766377.1843777989.1358654277.1358693980.1358696074.4; __utmz=150766377.1358654277.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); saved_searches=bin; __utmc=150766377; " + sCookie;
//        sPostData = "action=" + URLEncoder.encode("1", "UTF-8") + "&filter_category=" + URLEncoder.encode("lang_english", "UTF-8") + "&page=&price_range_min=" + URLEncoder.encode("0", "UTF-8") + "&price_range_max=" + URLEncoder.encode("15", "UTF-8") + "&domain_length_min=" + URLEncoder.encode("0", "UTF-8") + "&domain_length_max=" + URLEncoder.encode("22", "UTF-8") + "&date_filter_min=" + URLEncoder.encode("0", "UTF-8") + "&date_filter_max=" + URLEncoder.encode("9", "UTF-8") + "&user_list_type=" + URLEncoder.encode("1", "UTF-8") + "&order_col=" + URLEncoder.encode("name", "UTF-8") + "&order_dir=" + URLEncoder.encode("ASC", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("godrss", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("godclo", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("snaexp", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("nampre", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("dynadot", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("all", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("pendingdelete", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("cira", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("sedo", "UTF-8") + "&filter_auction[]=" + URLEncoder.encode("bido", "UTF-8") + "&dashes=" + URLEncoder.encode("1", "UTF-8") + "&digits=1&idn=" + URLEncoder.encode("1", "UTF-8") + "&starts_ends_type=" + URLEncoder.encode("1", "UTF-8") + "&starts_ends_bulk=&starts_ends=&tld_com=" + URLEncoder.encode("1", "UTF-8") + "&tld_net=" + URLEncoder.encode("1", "UTF-8") + "&result_count=" + URLEncoder.encode("1", "UTF-8") + "&current_url=";
        URLConnection urlConnection = null;
        urlConnection = new URL(sUrl).openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.setRequestProperty("Connection", "keep-alive");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0");
        urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        urlConnection.setRequestProperty("Cookie", sCookie);

        DataOutputStream outStream;
        DataInputStream inStream;
        outStream = new DataOutputStream(urlConnection.getOutputStream());

        outStream.writeBytes(sPostData);

        inStream = new DataInputStream(urlConnection.getInputStream());
        String buffer;
        String sReturn = null;
        while ((buffer = inStream.readLine()) != null)
        {
            sReturn += buffer;
        }
        inStream.close();
        outStream.flush();
        outStream.close();
        return sReturn;
    }

    private Map<String, List<String>> GetListHeaders(String sUrl) throws IOException
    {
        URLConnection urlConnection = new URL(sUrl).openConnection();
        return urlConnection.getHeaderFields();
    }

    private String GetCookie(String sUrl) throws IOException
    {
        URLConnection urlConnection = new URL(sUrl).openConnection();
        return urlConnection.getHeaderField("Set-Cookie");
    }

    public String DoGetFakePRGoogle(String sUrl) throws IOException
    {
        URL url = new URL(sUrl);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())))
        {
            output = "";
            String line;
            while ((line = reader.readLine()) != null)
            {
                output += line;
            }
        }
        return output;
    }

    public String DoGetCheckDomain_Alexa(String sUrl) throws IOException
    {
        URL url = new URL(sUrl);
        URLConnection conn = url.openConnection();
        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())))
        {
            output = "";
            String line;
            while ((line = reader.readLine()) != null)
            {
                output += line;
            }
        }
        return output;
    }

    public String DoPostCheckDomain_PageRanking(String sDomainName, String sPageName, String sPostData) throws IOException, NullPointerException
    {
        String sUrlPage = "http://www.pageranking.org/";
        String sReturn = null;
        String sUrl = sUrlPage + sPageName + ".php?query=" + sDomainName;
        URLConnection urlConnection = new URL(sUrl).openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        ((HttpURLConnection) urlConnection).setRequestMethod("POST");
        urlConnection.setRequestProperty("Accept", "text/html, */*; q=0.01");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.setRequestProperty("Connection", "keep-alive");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0");
        urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        urlConnection.setRequestProperty("Host", "www.pageranking.org");
        urlConnection.setRequestProperty("Referer", "http://www.pageranking.org/" + sDomainName);
        urlConnection.setRequestProperty("Cookie", "__utma=226780700.550724602.1359360347.1359360347.1359360347.1; __utmc=226780700; __utmz=226780700.1359360347.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __atuvc=1%7C5");
        urlConnection.setRequestProperty("Content-Length", "" + sPostData.length());

        DataOutputStream outStream = new DataOutputStream(urlConnection.getOutputStream());
        outStream.writeBytes(sPostData);
        DataInputStream inStream = new DataInputStream(urlConnection.getInputStream());
        String buffer;
        while ((buffer = inStream.readLine()) != null)
        {
            sReturn += buffer;
        }
        inStream.close();
        outStream.flush();
        outStream.close();
        return sReturn;
    }

    public String GetCookie_WebStatsDomain() throws IOException
    {
        String sCookie = null;
        int iCount = 0;
        while (sCookie == null && iCount < 5)
        {
            sCookie = GetCookie("http://www.webstatsdomain.com");
            iCount++;
        }
        if (sCookie == null)
        {
            return "__utma=265712393.969087244.1359720321.1359737894.1359779178.5; __utmz=265712393.1359720321.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __atuvc=4%7C5; PHPSESSID=2b73c880dc30f0036754187cad9a8160; __utmb=265712393.1.10.1359779178; __utmc=265712393; _ym_visorc=b";
        }
//        System.out.println("Cookie: " + sCookie);
        sCookie = sCookie.split(";")[0];
        sCookie = "__utma=265712393.969087244.1359720321.1359729644.1359737894.4; __utmz=265712393.1359720321.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __atuvc=3%7C5; __utmb=265712393.1.10.1359737894; _ym_visorc=b; " + sCookie + "; __utmc=265712393";
        return sCookie;
    }

    public String DoGetWebStatsDomain(String sUrl, String sCookie) throws IOException
    {
        URL url = new URL(sUrl);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Cookie", sCookie);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17");
        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())))
        {
            output = "";
            String line;
            while ((line = reader.readLine()) != null)
            {
                output += line;
            }
        }
        return output;
    }
}
