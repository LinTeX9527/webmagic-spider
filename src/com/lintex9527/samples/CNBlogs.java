package com.lintex9527.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Iterator;
import java.util.List;

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

        // 为什么始终只能得到第一个博文的日期呢？
        //*[@id="homepage1_HomePageDays_DaysList_ctl02_ImageLink"]
        //page.putField("dayTitle", page.getHtml().xpath("//div[@class='dayTitle']/a/text()").toString());

        // 通过这个解析，得到所有的博客日期
        List<String> days = page.getHtml().xpath("//div[@class='dayTitle']/a/text()").all();
//        for (int i = 0; i < days.size(); i ++){
//            System.out.println(days.get(i));
//        }


        // 发现后续链接，继续下一个爬虫
        // 如果下一个链接没有找到则停止爬虫
        //page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        // 起始页面
        Spider.create(new CNBlogs()).addUrl("http://www.cnblogs.com/LinTeX9527/").thread(2).run();
    }
}
