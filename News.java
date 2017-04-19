/**
 * Created by Jamali on 3/14/2017.
 */
public class News {
    private int id;
    public String title, link, cat, author;
    public News(String title, String link, String cat, String author) {
        this.title = title;
        this.link = link;
        this.cat = cat;
        this.author = author;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
