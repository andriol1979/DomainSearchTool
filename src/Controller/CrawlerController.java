/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import domainsearchtool.Context;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vutran
 */
public class CrawlerController
{
    public static String GetItemIDFromGodaddy(Context _context, String sDomainName) throws IOException
    {
        String sContent = _context.GetHttp().CrawlData("https://auctions.godaddy.com/trpItemListing.aspx?domain=" + sDomainName,
                "GET");
//        Util.File.CreateFile("godaddy.html", sContent);
        Pattern patternItem = Pattern.compile("<span\\s+class=\"OneLinkNoTx\">.*?</td><td\\s+style=.*?>(\\d+)</td><td\\s+style=\"border-right", Pattern.DOTALL);
        Matcher matcherItem = patternItem.matcher(sContent);
        if (matcherItem.find())
        {
            return matcherItem.group(matcherItem.groupCount());
        }
        return "";
    }
}
