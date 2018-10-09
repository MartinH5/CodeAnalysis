/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queries;

import entities.AuthorBook;
import entities.Book;
import entities.BookCity;
import entities.City;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLQueries {

    private static Connection getConnection() {
        try {
//            return DriverManager.getConnection("jdbc:mysql:/localhost:3306/gutenberg?user=root&password=root123&serverTimezone=UTC");
            //return DriverManager.getConnection("jdbc:mysql://localhost/guten?user=root&password=root&serverTimezone=UTC");
            return DriverManager.getConnection("jdbc:mysql://192.168.67.3/gutenberg?user=lasse&password=root&serverTimezone=UTC");
        } catch (SQLException ex) {
            System.out.println("Exception");
            System.out.println(ex.getMessage());
            return null;
        }

    }

    public static ArrayList<AuthorBook> getMentioningBooksWithAuthors(String cityname) throws SQLException, ClassNotFoundException {
        ArrayList<AuthorBook> ret = new ArrayList<>();
        Connection c;
        Statement s;
        ResultSet r;

        c = getConnection();
        s = c.createStatement();
        r = s.executeQuery("SELECT book_t.title, author_t.name FROM abc_relation_t "
                + "INNER JOIN book_t ON book_t.id = abc_relation_t.book_id "
                + "INNER JOIN author_t ON author_t.id = abc_relation_t.author_id "
                + "WHERE city_id = "
                + "(SELECT city_t.id FROM city_t "
                + "WHERE city_t.name = \"" + cityname + "\")");

        while (r.next()) {
            ret.add(new AuthorBook(r.getString("name"), r.getString("title")));
        }

        if (r != null) {
            r.close();
        }

        if (s != null) {
            s.close();
        }

        if (c != null) {
            c.close();
        }

        return ret;
    }

    public static ArrayList<City> getMentionedCities(String booktitle) throws SQLException {
        ArrayList<City> ret = new ArrayList<>();
        Connection c = null;
        Statement s = null;
        ResultSet r = null;

        c = getConnection();
        s = c.createStatement();
        r = s.executeQuery("SELECT city_t.name, city_t.latitude, city_t.longtitude "
                + "FROM abc_relation_t INNER JOIN book_t ON book_t.id = abc_relation_t.book_id "
                + "INNER JOIN city_t ON city_t.id = abc_relation_t.city_id "
                + "WHERE abc_relation_t.book_id = "
                + "(SELECT id FROM book_t WHERE book_t.title = \"" + booktitle + "\")");

        while (r.next()) {
            ret.add(new City(r.getString("name"), r.getFloat("latitude"), r.getFloat("longtitude")));
        }

        r.close();
        s.close();
        c.close();

        return ret;
    }

    public static ArrayList<BookCity> getBooksAndCities(String authorname) throws SQLException {
        ArrayList<BookCity> ret = new ArrayList<>();
        Connection c = null;
        Statement s = null;
        ResultSet r = null;

        c = getConnection();
        s = c.createStatement();
        r = s.executeQuery("SELECT book_t.title, city_t.name, city_t.latitude, city_t.longtitude "
                + "FROM abc_relation_t JOIN city_t ON city_t.id = abc_relation_t.city_id "
                + "INNER JOIN book_t ON book_t.id = abc_relation_t.book_id "
                + "WHERE abc_relation_t.author_id = "
                + "(SELECT author_t.id FROM author_t WHERE author_t.name = \"" + authorname + "\")");

        while (r.next()) {
            ret.add(new BookCity(r.getString("title"), r.getString("name"), r.getFloat("latitude"), r.getFloat("longtitude")));
        }

        r.close();
        s.close();
        c.close();

        return ret;
    }

    public static ArrayList<Book> getBooksMentioningRange(float latitude, float longtitude, float leeway) throws SQLException {
        ArrayList<Book> ret = new ArrayList<>();
        Connection c = null;
        Statement s = null;
        ResultSet r = null;

        ArrayList<Integer> cityIds = new ArrayList<>();

        c = getConnection();
        s = c.createStatement();
        r = s.executeQuery(
                "SELECT * FROM city_t WHERE latitude > " + (latitude - leeway)
                + " AND latitude < " + (latitude + leeway)
                + " AND longtitude > " + (longtitude - leeway)
                + " AND longtitude < " + (longtitude + leeway));

        while (r.next()) {
            cityIds.add(r.getInt("id"));
        }

        r.close();
        s.close();

        for (Integer i : cityIds) {
            s = c.createStatement();
            r = s.executeQuery("SELECT book_t.title FROM book_city_relation_t JOIN book_t ON book_t.id = book_city_relation_t.book_id WHERE city_id = " + i);

            while (r.next()) {

                ret.add(new Book(r.getString(1)));
            }

            r.close();
            s.close();
        }

        c.close();

        return ret;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        for (Book b : getBooksMentioningRange(31, 65, 1)) {
            System.out.println(b.getTitle());
        }
    }

}
