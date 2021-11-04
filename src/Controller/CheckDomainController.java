/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: CheckDomainController.java
 * Ngày tạo: Jan 28, 2013, 3:20:13 PM
 * Ngày sửa: Jan 28, 2013, 3:20:13 PM
 * Diễn giải:
 * Copyright 2010
 */
package Controller;

import Entity.CheckDomainObject;
import Entity.DomainAgeObject;
import domainsearchtool.Context;
import domainsearchtool.JFrameCheckDomainAgePleaseWait;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckDomainController
{
    Context _context;
    CheckDomainObject oCheck;
    String sPostData;
    final int NUM_JOBS = 3;

    public CheckDomainController(Context context) throws UnsupportedEncodingException
    {
        _context = context;
        oCheck = new CheckDomainObject();
        sPostData = "rndNum=" + URLEncoder.encode("46141.2976397321", "UTF-8");
    }
    public CheckDomainController(Context context, CheckDomainObject oCheck) throws UnsupportedEncodingException
    {
        _context = context;
        this.oCheck = oCheck;
        sPostData = "rndNum=" + URLEncoder.encode("46141.2976397321", "UTF-8");
    }

    public CheckDomainObject GetCheckDomainAgeObject(final String sDomain) throws InterruptedException, IOException
    {
        oCheck.setsDomainName(sDomain);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletionService completion = new ExecutorCompletionService(executor);

        completion.submit(new PostDomainAge(this, sDomain));
        // wait for all tasks to complete.
        for (int i = 0; i < 1; ++i)
        {
            completion.take(); // will block until the next sub task has completed.
        }
        executor.shutdown();
//        System.out.println("End of thread: " + oCheck.getoDoMainAge());
        return oCheck;
    }
    
    public CheckDomainObject GetCheckDomainObject(final String sDomain) throws InterruptedException, IOException
    {
        oCheck.setsDomainName(sDomain);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_JOBS);
        CompletionService completion = new ExecutorCompletionService(executor);

//        completion.submit(new PostDomainAge(this, sDomain));
        completion.submit(new GetAlexaInfomation(this, sDomain));
        completion.submit(new PostGooglePageRanking(this, sDomain));
        completion.submit(new PostDmoz(this, sDomain));

        // wait for all tasks to complete.
        for (int i = 0; i < NUM_JOBS; ++i)
        {
            completion.take(); // will block until the next sub task has completed.
        }
        executor.shutdown();
//        System.out.println("End of thread");
        return oCheck;
    }
}

class GetAlexaInfomation implements Callable<Object>
{
    private CheckDomainController controller;
    private String sDomain;

    public GetAlexaInfomation(CheckDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sUrl = "http://www.pageranking.org/" + sDomain;
        try
        {
            String sContent = controller._context.GetHttp().DoGetCheckDomain_Alexa(sUrl);
//            Util.File.CreateFile("alexa.html", sContent);
            Pattern patternItem = Pattern.compile("Alexa Rank.*?<div class=\"portlet-content\">.*?<table\\s>.*?<b>Domain</b>.*?<b>\\sCreated\\s</b>.*?<tr align=\"center\">.*?/td><td.*?>(.*?)</td><td.*?>(.*?)</td>", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            if (matcherItem.find())
            {
                controller.oCheck.setiAlexaPagerank(matcherItem.group(1).trim().length() == 0 ? 0
                        : Integer.parseInt(matcherItem.group(1).trim()));
                controller.oCheck.setiBacklinks(matcherItem.group(2).trim().length() == 0 ? 0
                        : Integer.parseInt(matcherItem.group(2).trim()));
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(GetAlexaInfomation.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish GetAlexaInfomation");
        return null;
    }
}

class PostGooglePageRanking implements Callable<Object>
{
    private CheckDomainController controller;
    private String sDomain;

    public PostGooglePageRanking(CheckDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sPageName = "subpagerank";
        try
        {
            String sContent = controller._context.GetHttp().DoPostCheckDomain_PageRanking(sDomain,
                    sPageName, controller.sPostData);
//            Util.File.CreateFile("googlepr.html", sContent);
            Pattern patternItem = Pattern.compile("has a page rank.*?<b>(.*?)</b>", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            if (matcherItem.find())
            {
                controller.oCheck.setiGooglePagerank(matcherItem.group(1).trim().length() == 0 ? 0
                        : Integer.parseInt(matcherItem.group(1).trim()));
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(PostGooglePageRanking.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish PostGooglePageRanking");
        return null;
    }
}

class PostDmoz implements Callable<Object>
{
    private CheckDomainController controller;
    private String sDomain;

    public PostDmoz(CheckDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        final String sDmozNotApply = "n/a";
        String sPageName = "subdmoz";
        try
        {
            String sContent = controller._context.GetHttp().DoPostCheckDomain_PageRanking(sDomain,
                    sPageName, controller.sPostData);
//            Util.File.CreateFile("dmoz.html", sContent);
            Pattern patternItem = Pattern.compile("Listed in DMOZ:<b>(.*?)</b></td>", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            if (matcherItem.find())
            {
                controller.oCheck.setIsDmoz(matcherItem.group(1).trim().length() == 0 ? false
                        : (matcherItem.group(1).trim().equalsIgnoreCase(sDmozNotApply) ? false : true));
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(PostDmoz.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish PostDmoz");
        return null;
    }
}

class PostDomainAge implements Callable<Object>
{
    private CheckDomainController controller;
    private String sDomain;

    public PostDomainAge(CheckDomainController controller, String sDomain)
    {
        this.controller = controller;
        this.sDomain = sDomain;
    }

    @Override
    public Object call() throws Exception
    {
        String sPageName = "subage";
        try
        {
            String sContent = controller._context.GetHttp().DoPostCheckDomain_PageRanking(sDomain,
                    sPageName, controller.sPostData);
//            Util.File.CreateFile("age.html", sContent);
            Pattern patternItem = Pattern.compile("</b> was created on: <b>(.*?)</b></p>", Pattern.DOTALL);
            Matcher matcherItem = patternItem.matcher(sContent);
            if (matcherItem.find())
            {
                controller.oCheck.setoDoMainAge(GetDomainAgeObject(matcherItem.group(1)));
            }
            else
            {
                controller.oCheck.setoDoMainAge(GetDomainAgeObject("Not found"));
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(PostDomainAge.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Finish PostDomainAge");
        return null;
    }

    private DomainAgeObject GetDomainAgeObject(String sContent)
    {
        DomainAgeObject oDomainAge = new DomainAgeObject();
        oDomainAge.setsDomainAge(sContent);
        Pattern patternItem = Pattern.compile("(\\d+).*?(\\d+)", Pattern.DOTALL);
        Matcher matcherItem = patternItem.matcher(sContent);
        if (matcherItem.find())
        {
            int iAge = Integer.parseInt(matcherItem.group(1)) * 365 + Integer.parseInt(matcherItem.group(2));
            oDomainAge.setiDomainAge(iAge);
        }
        else
        {
            oDomainAge.setiDomainAge(0);
        }
        return oDomainAge;
    }
}