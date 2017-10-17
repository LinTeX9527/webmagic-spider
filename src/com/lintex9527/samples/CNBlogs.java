package com.lintex9527.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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

        //-----------------------  博客日期 OK--------------------------
        // 为什么始终只能得到第一个博文的日期呢？
        //*[@id="homepage1_HomePageDays_DaysList_ctl02_ImageLink"]
        //page.putField("dayTitle", page.getHtml().xpath("//div[@class='dayTitle']/a/text()").toString());

        // 通过这个解析，得到所有的博客日期
        List<String> days = page.getHtml().xpath("//div[@class='dayTitle']/a/text()").all();
        for (int i = 0; i < days.size(); i ++){
            System.out.println(days.get(i));
        }

        // ----------------------  博文链接  OK------------------------
        // 原始的 a 标签如下，想要获取 <a href='xxx'></a> 中 href 的内容，对于的 XPath 就是 /a/@href
        //<a id="homepage1_HomePageDays_DaysList_ctl04_DayList_TitleUrl_0" class="postTitle2" href="http://www.cnblogs.com/LinTeX9527/p/7298278.html">STM32 内存管理实验</a>
        List<String> bloglinks = page.getHtml().xpath("//div[@class='postTitle']/a/@href").all();
        for (int i = 0; i < bloglinks.size(); i ++){
            System.out.println(bloglinks.get(i));
        }

        // 发现后续链接，继续下一个爬虫
        // 如果下一个链接没有找到则停止爬虫
        // 按钮“下一页”的 XPATH 表达式
        //*[@id="nav_next_page"]/a
        //div[@class='topicListFooter']/div[@id='nav_next_page']/a/@href

        //List<String> pagelists = page.getHtml().xpath("//div[@class='topicListFooter']/div/a/@href").all();
        String nextpage = page.getHtml().xpath("//div[@class='topicListFooter']/div/a[starts-with(text(), '下一页')]/@href").toString();
        if (!nextpage.equals("")){
            System.out.println("下一页的链接是：" + nextpage);
            page.addTargetRequest(nextpage);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        // 起始页面，是用户博文列表页面的第一页
        //Spider.create(new CNBlogs()).addUrl("http://www.cnblogs.com/LinTeX9527/").thread(2).run();
        //http://www.cnblogs.com/xueweihan/default.html?page=2
        Spider.create(new CNBlogs()).addUrl("http://www.cnblogs.com/xueweihan/default.html?page=2").thread(2).run();
    }
}
