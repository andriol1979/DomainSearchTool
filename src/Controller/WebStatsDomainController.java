/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.OrganicKeywordsObject;
import Entity.SocialActivityObject;
import Entity.WebStatsDomainObject;
import domainsearchtool.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vutran
 */
public class WebStatsDomainController
{
    WebStatsDomainObject oWebStats;
    Context _context;
    String sCookie;
    final int NUM_JOBS = 5;

    public WebStatsDomainController(Context context)
    {
        _context = context;
        oWebStats = new WebStatsDomainObject();
    }

    public WebStatsDomainObject GetWebStatsDomainObject(final String sDomain) throws InterruptedException, IOException
    {
        //Get cookie
        _context.GetHttp().GetCookie_WebStatsDomain();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_JOBS);
        CompletionService completion = new ExecutorCompletionService(executor);

        completion.submit(new CheckWebsiteWorth_SEOScore(this, sDomain));
        completion.submit(new CheckWebsiteOrganicKeywords(this, sDomain));
        completion.submit(new CheckWebsiteSocial_Facebook(this, sDomain));
        completion.submit(new CheckWebsiteSocial_Twitter(this, sDomain));
        completion.submit(new CheckWebsiteSocial_GooglePlus(this, sDomain));

        // wait for all tasks to complete.
        for (int i = 0; i < NUM_JOBS; ++i)
        {
            completion.take(); // will block until the next sub task has completed.
        }
        executor.shutdown();
//        System.out.println("End of thread");
        return oWebStats;
    }
}

class CheckWebsiteWorth_SEOScore implements Callable<Object>
{
    private WebStatsDomainController controller;
    private String sDomain;

    public CheckWebsiteWorth_SEOScore(WebStatsDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sUrl = "http://www.webstatsdomain.com/load_domen_worth_and_seo_percent.php?domain="
                + sDomain + "&rand=0.9334092806913141";
        try
        {
            String sContent = controller._context.GetHttp().DoGetWebStatsDomain(sUrl, controller.sCookie);
//            Util.File.CreateFile("seoscore.html", sContent);
            Pattern patternItem = Pattern.compile("worth=(.*?)end_worthseo_percent=(.*?)end_seo_percent", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            if (matcherItem.find())
            {
                controller.oWebStats.setsWebsiteWorth(matcherItem.group(1));
                controller.oWebStats.setsSeoScore(matcherItem.group(2));
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(CheckWebsiteWorth_SEOScore.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish CheckWebsiteWorth_SEOScore");
        return null;
    }
}

class CheckWebsiteOrganicKeywords implements Callable<Object>
{
    private WebStatsDomainController controller;
    private String sDomain;

    public CheckWebsiteOrganicKeywords(WebStatsDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sUrl = "http://www.webstatsdomain.com/domains/" + sDomain + "/";
        try
        {
            String sContent = controller._context.GetHttp().DoGetWebStatsDomain(sUrl, controller.sCookie);
//            Util.File.CreateFile("seoscore.html", sContent);
            Pattern patternItem = Pattern.compile("href=\"/keywords/.*?_blank\">(.*?)</a>\\s+</td>\\s+<td.*?title.*?\">(.*?)</td>\\s+<td.*?\">(.*?)</td>\\s+<td.*?\">(.*?)</td>\\s+<td.*?\">(.*?)</td>", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            List<OrganicKeywordsObject> list = new ArrayList<>();
            while (matcherItem.find())
            {
                OrganicKeywordsObject oOrganic = new OrganicKeywordsObject();
                oOrganic.setsKeyword(matcherItem.group(1));
                oOrganic.setsUrl(matcherItem.group(2));
                oOrganic.setsTotalResult(matcherItem.group(3));
                oOrganic.setsPosition(matcherItem.group(4));
                oOrganic.setsLastChecked(matcherItem.group(5));
                list.add(oOrganic);
            }
            controller.oWebStats.setListOrganic(list);
        }
        catch (IOException ex)
        {
            Logger.getLogger(CheckWebsiteWorth_SEOScore.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish CheckWebsiteOrganicKeywords");
        return null;
    }
}

class CheckWebsiteSocial_Twitter implements Callable<Object>
{
    private WebStatsDomainController controller;
    private String sDomain;

    public CheckWebsiteSocial_Twitter(WebStatsDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sUrl = "http://urls.api.twitter.com/1/urls/count.json?url=http://" + sDomain + "&callback=jsonp1359730014320";
        try
        {
            String sContent = controller._context.GetHttp().CrawlData(sUrl, "GET");
//            Util.File.CreateFile("seoscore.html", sContent);
            Pattern patternItem = Pattern.compile("\"count\":(\\d+),\"url\"", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            SocialActivityObject oSocial = new SocialActivityObject();
            oSocial.setsSocialName("Count Tweets");
            oSocial.setsImageName("twitter_count.png");
            if (matcherItem.find())
            {
                oSocial.setsValue(matcherItem.group(1));
            }
            else
            {
                oSocial.setsValue("0");
            }
            controller.oWebStats.AddListSocial(oSocial);
        }
        catch (IOException ex)
        {
            Logger.getLogger(CheckWebsiteSocial_Twitter.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish CheckWebsiteSocial_Twitter");
        return null;
    }
}

class CheckWebsiteSocial_Facebook implements Callable<Object>
{
    private WebStatsDomainController controller;
    private String sDomain;

    public CheckWebsiteSocial_Facebook(WebStatsDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sUrl = "http://api.facebook.com/restserver.php?method=links.getStats&urls=http://" + sDomain + "&callback=&callback=jsonp1359782787658";
        try
        {
            String sContent = controller._context.GetHttp().CrawlData(sUrl, "GET");
//            Util.File.CreateFile("seoscore.html", sContent);
            Pattern patternItem = Pattern.compile("<share_count>(\\d+)</share_count>.*?<like_count>(\\d+)</like_count>.*?<comment_count>(\\d+)</comment_count>", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            //Facebook shares
            SocialActivityObject oSocialShare = new SocialActivityObject();
            oSocialShare.setsSocialName("Facebook Shares");
            oSocialShare.setsImageName("facebook_share.png");
            //Facebook likes
            SocialActivityObject oSocialLike = new SocialActivityObject();
            oSocialLike.setsSocialName("Facebook Likes");
            oSocialLike.setsImageName("facebook_like.png");
            //Facebook comments
            SocialActivityObject oSocialComment = new SocialActivityObject();
            oSocialComment.setsSocialName("Facebook Comments");
            oSocialComment.setsImageName("facebook_comment.png");
            if (matcherItem.find())
            {
                oSocialShare.setsValue(matcherItem.group(1));
                oSocialLike.setsValue(matcherItem.group(2));
                oSocialComment.setsValue(matcherItem.group(3));
            }
            else
            {
                oSocialShare.setsValue("0");
                oSocialLike.setsValue("0");
                oSocialComment.setsValue("0");
            }
            controller.oWebStats.AddListSocial(oSocialComment);
            controller.oWebStats.AddListSocial(oSocialLike);
            controller.oWebStats.AddListSocial(oSocialShare);
        }
        catch (IOException ex)
        {
            Logger.getLogger(CheckWebsiteSocial_Facebook.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish CheckWebsiteSocial_Facebook");
        return null;
    }
}

class CheckWebsiteSocial_GooglePlus implements Callable<Object>
{
    private WebStatsDomainController controller;
    private String sDomain;

    public CheckWebsiteSocial_GooglePlus(WebStatsDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sUrl = "http://query.yahooapis.com/v1/public/yql?callback=jsonp1359784758904&q=select+*+from+html+where+url%3D%22https%3A%2F%2Fplusone.google.com%2Fu%2F0%2F_%2F%252B1%2Ffastbutton%3Fcount%3Dtrue%26url%3Dhttp%3A%2F%2F" + sDomain + "%22+and+xpath%3D%22*%22&format=xml";
        try
        {
            String sContent = controller._context.GetHttp().CrawlData(sUrl, "GET");
//            Util.File.CreateFile("seoscore.html", sContent);
            Pattern patternItem = Pattern.compile("<div class=\\\\\"V1\\\\\" id=\\\\\"aggregateCount\\\\\">\\\\n.*?<p>(\\d+)<\\\\/p>\\\\n", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            SocialActivityObject oSocial = new SocialActivityObject();
            oSocial.setsSocialName("Google+");
            oSocial.setsImageName("google_plus.png");
            if (matcherItem.find())
            {
                oSocial.setsValue(matcherItem.group(1));
            }
            else
            {
                oSocial.setsValue("0");
            }
            controller.oWebStats.AddListSocial(oSocial);
        }
        catch (IOException ex)
        {
            Logger.getLogger(CheckWebsiteSocial_GooglePlus.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish CheckWebsiteSocial_GooglePlus");
        return null;
    }
}