import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Jamali on 3/15/2017.
 */
public class Scraper {
    private Queue linksQueue;
    private DA dataAccess;
    private int maxNews;
    public Scraper(int max) throws Exception{
        linksQueue = new LinkedList();
        dataAccess = new DA();
        maxNews = max;

        for (int i = 1; i < 7; i++) {
            initQueue("http://www.zoomit.ir/page/" + i + "/");
        }
    }
    private void initQueue(String page) throws Exception{
        Document document = Jsoup.connect(page).get();
        Elements newsLinks = document.select("#ArticleDetails div.item-list-row div.item-list-text");
        for (Element newsLink : newsLinks) {
            String link = newsLink.select("h3 a").get(0).attr("href");
            if (link.contains("zoomit.ir/")) {
                linksQueue.add(link);
                System.out.println("Added: " + link);
            }
        }
    }
    public void startScrape() throws Exception {
        while (linksQueue.size() > 0 && dataAccess.getConutOfAllNews() < maxNews) {
            System.out.println(dataAccess.getConutOfAllNews() + " Links scraped.");
            System.out.println(linksQueue.size() + " Links in queue.");
            scrape((String) linksQueue.peek());
            linksQueue.remove();
        }
        System.out.println("Scraping finished...");
        System.out.println("");
    }
    private void scrape (String scrapingLink) throws Exception{
        System.out.println("Scraping: " + scrapingLink);
        Document document = Jsoup.connect(scrapingLink).parser(Parser.xmlParser()).ignoreContentType(true).get();
        Elements newsLinks = document.select("#ArticleDetails");
        for (Element newsLink : newsLinks) {
            String title = newsLink.select("div.article-header h1 a").get(0).text();
            String link = newsLink.select("div.article-header h1 a").get(0).attr("href");
            String cat = newsLink.select("div.article-header li.catitem a").get(0).html();
            String author = newsLink.select("div.author-details div.author-details-row1 span a[href]").get(0).html();

            Elements relatedPosts = newsLink.select("div.article-content div.article-related-row div.relatedapep div.col-sm-4 h4 a[href]");
            for (Element relatedPost: relatedPosts) {
                if (dataAccess.isLinkRepeated(relatedPost.attr("href")) == 0 && relatedPost.attr("href").contains("zoomit.ir/")) {
                    linksQueue.add(relatedPost.attr("href"));
                    System.out.println("Added: " + relatedPost.attr("href"));
                }
            }
            if (dataAccess.isLinkRepeated(link) == 0) {
                News n = new News(title, link, cat, author);
                dataAccess.insert(n);
            }
        }
    }
}
