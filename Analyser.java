import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Baran on 3/18/2017.
 */
public class Analyser {
    private DA dataAccess;
    private ArrayList<String> allAuthors;

    public Analyser() throws Exception {
        allAuthors = new ArrayList<String>();
        dataAccess = new DA();
    }
    public void startAnalyse() throws
            Exception {
        System.out.println("Analyse started...");
        ResultSet resultSet = dataAccess.getAllOfNews();
        while (resultSet.next()) {
            if (!allAuthors.contains(resultSet.getString("news_author")))
                allAuthors.add(resultSet.getString("news_author"));
        }
        for (String author: allAuthors) {
            System.out.println("Name: " + author);
            System.out.println("Count of news: " + dataAccess.getConutOfAuthorNews(author));
            System.out.println("Favorite cat: " + dataAccess.getAuthorFavoriteCat(author));
            System.out.println();
        }

    }
}
