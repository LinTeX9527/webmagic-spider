package com.lintex9527.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;

/**
 *
 * 爬取 cnblogs 我的个人博客地址
 * http://www.cnblogs.com/LinTeX9527/
 *
 * 2017/10/17 15:26
 * 1、有可能网页是动态的，需要加入 user-agent
 * 2、正则表达式不对
 * 3、
 */
public class CNBlogs implements PageProcessor{

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {

        // 月份列表的地址格式
        //http://www.cnblogs.com/xueweihan/archive/2017/09/21.html,
        String URL_POST = "http://www\\.cnblogs\\.com/xueweihan/archive/\\d+/\\d+\\.html";

        // 调试，本页地址
        System.out.println("本页地址是：" + page.getUrl().toString());

        // 打印网页，调试用途
        System.out.println(page.getHtml().getDocument().toString());



        // 找到用户的“随笔档案”中的每个月份的地址链接
        String expression = "//div[@id=\"sidebar_postarchive\"]/ul";
        //page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all()
        //ArrayList<String> monthpages = (ArrayList<String>) page.getHtml().links().regex(URL_POST).all();
        ArrayList<String> monthpages = (ArrayList<String>) page.getHtml().links().all();
        if (monthpages != null){
            System.out.println("随笔档案的链接是：" + monthpages.toString());
            //page.addTargetRequest(monthpages);
        }
    }

    @Override
    public Site getSite() {
        site.setDomain("www.diandian.com")
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
        return site;
    }

    public static void main(String[] args) {

        // 经过观察，可以从用户首页开始，按照时间归档查找用户每个月份发表的博文
        String start_url = "http://www.cnblogs.com/xueweihan/";
        Spider.create(new CNBlogs()).addUrl(start_url).thread(2).run();

    }
}
