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

    // 用户侧边栏---“随笔档案”的请求链接
    public static final String LINK_SIDECOLUMN = "http://www.cnblogs.com/username/mvc/blog/sidecolumn.aspx?blogApp=username";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    /**
     * 从用户的“随笔档案”开始查找用户所有的博客地址链接，具体步骤如下：
     * 1、通过“随笔档案”得到月份列表，每一个月份包括用户当月发出了多少个随笔；
     * 2、解析月份列表，得到当月用户所有的随笔，即单个博文的地址；
     * 3、解析当个博文，可以下载博文正文，统计信息等。（如果只是统计博文的阅读数、点赞数，可以在步骤2搞定）
     * @param page
     */
    @Override
    public void process(Page page) {

        // 月份列表的地址格式
        //http://www.cnblogs.com/xueweihan/archive/2017/09/21.html,

        // 本页地址
        String page_url = page.getUrl().toString();
        System.out.println("本页地址是：" + page_url);

        // 调试用途，打印网页内容
        //String page_content = page.getHtml().getDocument().toString();
        //System.out.println(page_content);

        // 根据当前网页地址，查找不同的网络地址链接
        // 如果是“随笔档案”，则找到所有的月份列表页
        if (page_url.contains("mvc/blog/sidecolumn.aspx?blogApp")){
            // 找到用户的“随笔档案”中的每个月份的地址链接
            String expression = "//div[@id='sidebar_postarchive']/ul/li/a/@href";
            ArrayList<String> monthpages = (ArrayList<String>) page.getHtml().xpath(expression).all();
            if (monthpages != null){
                System.out.println("随笔档案的列表项是：");
                // 将所有的月份列表页加载并解析
                for (int i = 0; i < monthpages.size(); i++) {
                    System.out.println("" + i + " : " + monthpages.get(i));
                    page.addTargetRequest(monthpages.get(i));
                }
            }
        } else if (page_url.contains("archive")){
            // 如果是月份列表页
            // http://www.cnblogs.com/LinTeX9527/archive/2014/08.html
            //div[@class='entrylistItem']/div[@class='entrylistPosttitle']/a[@class='entrylistItemTitle']/@href
            // 查找月份列表页当中所有的博文链接的地址
            String expre_blog = "//div[@class='entrylistItem']/div[@class='entrylistPosttitle']/a[@class='entrylistItemTitle']/@href";
            ArrayList<String> bloglists = (ArrayList<String>) page.getHtml().xpath(expre_blog).all();
            if (bloglists != null){

                page.addTargetRequests(bloglists);
                // 调试
//                for (int i = 0; i < bloglists.size(); i++) {
//                    System.out.println("博文地址---------->" + bloglists.get(i).toString());
//                }
            }
        } else if (page_url.matches("http://www.cnblogs.com/(\\w+)/p/(\\d+).html")){
            // 博文页面，可以查看博文内容
            // http://www.cnblogs.com/LinTeX9527/p/7280360.html
            //div[@id='topics']/div[@class='post']/h1[@class='postTitle']/a/text()
            String expre_title = "//div[@id='topics']/div[@class='post']/h1[@class='postTitle']/a/text()";
            String blog_title = page.getHtml().xpath(expre_title).toString();
            System.out.println("博文标题------------------->" + blog_title);

            // 博文发布日期
            //div[@class='postDesc']/span[@id='post-date']/text()
            String expre_date = "//div[@class='postDesc']/span[@id='post-date']/text()";
            String blog_date = page.getHtml().xpath(expre_date).toString();
            System.out.println("发布日期------------------>" + blog_date);
        }
    }

    @Override
    public Site getSite() {
        site.setDomain("www.diandian.com")
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
        return site;
    }

    public static void main(String[] args) {

        // 用户名不区分大小写
        String username = "lintex9527";

        // 构造用户侧边栏 --- “随笔档案”的请求链接
        String link_sidecolumn = LINK_SIDECOLUMN.replaceAll("username", username);
        System.out.println("随笔档案地址：" + link_sidecolumn);


        // 经过观察，可以从用户首页开始，按照时间归档查找用户每个月份发表的博文
        String start_url = link_sidecolumn;
        Spider.create(new CNBlogs()).addUrl(start_url).thread(2).run();

    }
}
