import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Jamali on 3/13/2017.
 */
public class DA {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public DA() throws Exception {
        //Class.forName("oracle.jdbc.driver.OracleDriver");
        //connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "123");
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_db?characterEncoding=utf8", "root", "");
    }
    public void update(News news) throws Exception {
        preparedStatement = connection.prepareStatement("update news set title = ?, link = ?, cat = ?, author = ? ");
        preparedStatement.setString(1, news.title);
        preparedStatement.setString(2, news.link);
        preparedStatement.setString(3, news.cat);
        preparedStatement.setString(4, news.author);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void delete(News news) throws Exception {
        preparedStatement = connection.prepareStatement("delete from news where title = ? and link = ? and cat = ? and author = ?");
        preparedStatement.setString(1, news.title);
        preparedStatement.setString(2, news.link);
        preparedStatement.setString(3, news.cat);
        preparedStatement.setString(4, news.author);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void insert(News news) throws Exception {
        //preparedStatement = connection.prepareStatement("Create sequence news_sequence start with 1 increment by 1 minvalue 1 maxvalue 10000");
        //preparedStatement = connection.prepareStatement("insert into news (news_id, news_title, news_link, news_cat, news_author) values (news_sequence.NEXTVAL,?,?,?,?)");
        preparedStatement = connection.prepareStatement("insert into news (news_title, news_link, news_cat, news_author) values (?,?,?,?)");
        preparedStatement.setString(1, news.title);
        preparedStatement.setString(2, news.link);
        preparedStatement.setString(3, news.cat);
        preparedStatement.setString(4, news.author);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void showAll() throws Exception {
        preparedStatement = connection.prepareStatement("select * from news");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("#" + resultSet.getInt("news_id") + ":");
            System.out.println("Title: " + resultSet.getString("news_title"));
            System.out.println("Link: " + resultSet.getString("news_link"));
            System.out.println("Cat: " + resultSet.getString("news_cat"));
            System.out.println("Author: " + resultSet.getString("news_author"));
            System.out.println();
        }
        resultSet.close();
        preparedStatement.close();
    }
    public ResultSet selectByAuthor(String author) throws Exception {
        preparedStatement = connection.prepareStatement("select * from news where news_author = ?");
        preparedStatement.setString(1, author);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public int getConutOfAuthorNews(String author) throws Exception {
        preparedStatement = connection.prepareStatement("select count(*) from news where news_author = ?");
        preparedStatement.setString(1, author);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        preparedStatement.close();
        return count;
    }
    private int getCountOfNewsWithCatAndAuthor(String author, String cat) throws Exception{
        preparedStatement = connection.prepareStatement("select count(*) from news where news_author = ? and news_cat = ?");
        preparedStatement.setString(1, author);
        preparedStatement.setString(2, cat);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        preparedStatement.close();
        return count;
    }
    public String getAuthorFavoriteCat(String author) throws Exception{
        ResultSet authorNews = selectByAuthor(author);
        ArrayList<String> authorNewsCats = new ArrayList<String>();
        while (authorNews.next()) {
            if(!authorNewsCats.contains(authorNews.getString("news_cat")))
                authorNewsCats.add(authorNews.getString("news_cat"));
        }
        int[] counts = new int[authorNewsCats.size()];
        for (String cat: authorNewsCats) {
            counts[authorNewsCats.indexOf(cat)] = getCountOfNewsWithCatAndAuthor(author, cat);
        }
        int indexOfMax = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] > counts[indexOfMax])
                indexOfMax = i;
        }
        return authorNewsCats.get(indexOfMax);
    }
    public int isLinkRepeated(String link) throws Exception{
        preparedStatement = connection.prepareStatement("select count(*) from news where news_link = ?");
        preparedStatement.setString(1, link);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        preparedStatement.close();
        return count;
    }
    public int getConutOfAllNews() throws Exception {
        preparedStatement = connection.prepareStatement("select count(*) from news");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        preparedStatement.close();
        return count;
    }
    public ResultSet getAllOfNews() throws Exception {
        preparedStatement = connection.prepareStatement("select * from news");
        return preparedStatement.executeQuery();
    }

    //CREATE TABLE news ( news_id number(10) NOT NULL, news_title varchar2(255) NOT NULL, news_link varchar2(255) NOT NULL, news_cat varchar2(63) NOT NULL, news_author varchar2(128) NOT NULL, constraint pk_news_id PRIMARY KEY(news_id));
    //Create sequence news_sequence start with 1 increment by 1 minvalue 1 maxvalue 10000;
    //insert into news (news_id, news_title, news_link, news_cat, news_author) values (news_sequence, 'title', 'link', 'cat', 16);


}
