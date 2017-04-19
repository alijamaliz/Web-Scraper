/**
 * Created by Jamali on 3/14/2017.
 */
public class Main{
    public static void main(String[]args) throws Exception {
        Scraper scraper = new Scraper(400);
        scraper.startScrape();

        Analyser analyser = new Analyser();
        analyser.startAnalyse();
    }
}
