package com.lintex9527.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 *
 * 爬取糗事百科热门
 * https://www.qiushibaike.com/hot/page/2/
 *
 * 2017/10/17 15:26
 * 没有抓取到作者的名字，什么原因导致的？
 * 1、有可能网页是动态的，需要加入 user-agent
 * 2、正则表达式不对
 * 3、
 */
public class Qiubai implements PageProcessor{

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {


        //page.putField("username", page.getHtml().xpath("//*/div[1]/a[2]/h2/text()").toString());
        page.putField("username", page.getHtml().xpath("//div[@class='author clearfix']/a[2]/h2/text()").toString());
        page.putField("jokecontent", page.getHtml().xpath("//a[@class='contentHerf']/div[@class='content']/span/text()").toString());

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
        Spider.create(new Qiubai()).addUrl("https://www.qiushibaike.com/hot/page/2/").thread(2).run();
    }
}
