/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queries;

import com.mycompany.gutenbergproject.connections.Neo4jConnection;
import entities.AuthorBook;
import entities.Book;
import entities.BookCity;
import entities.City;
import java.util.ArrayList;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

/**
 *
 * @author Micha
 */
public class Neo4jQueries {

     public static ArrayList<AuthorBook> getMentioningBooksWithAuthors(String cityname) {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<AuthorBook> books = new ArrayList<>();

        Session session = driver.session();
        String query = "MATCH ({name:" + cityname + "})<-[:MENTIONS]-(book:Book)<-[:WRITTEN]-(author:Author)\n"
                + "RETURN book.title AS title, author.name AS name";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(new AuthorBook(record.get("title").asString(), record.get("name").asString()));
        }
        return books;
    }

    public static ArrayList<BookCity> getBooksAndCities(String authorname) {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<BookCity> books = new ArrayList<>();

        Session session = driver.session();
        String query = "MATCH ({name:" + authorname + "})-[:WRITTEN]->(book:Book)-[:MENTIONS]->(city:City)\n"
                + "RETURN book.title AS title, city.name AS name, city.latitude AS latitude, city.longitude AS longitude";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(new BookCity(record.get("title").asString(), record.get("name").asString(), record.get("latitude").asFloat(), record.get("longitude").asFloat()));
        }
        return books;
    }

    public static ArrayList<City> mentionedCities(String booktitle) {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<City> books = new ArrayList<>();

        Session session = driver.session();
        String query = "MATCH ({title:" + booktitle + "})-[:MENTIONS]->(city:City)\n"
                + "RETURN city.name AS name, city.latitude AS latitude, city.longitude AS longitude";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(new City(record.get("name").asString(), record.get("latitude").asFloat(), record.get("longitude").asFloat()));
        }
        return books;
    }

    public static ArrayList<Book> getBooksMentioningRange(double latitude, double longtitude, int leeway) {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<Book> books = new ArrayList<>();

        Session session = driver.session();
        String query = "WITH " + latitude + " AS lat, " + longtitude + " AS lon\n"
                + "MATCH (l:City) \n"
                + "WHERE 2 * 6371 * asin(sqrt(haversin(radians(lat - toFloat(split(l.name, \",\")[0])))+ cos(radians(lat))* cos(radians(toFloat(split(l.name, \",\")[0])))* haversin(radians(lon - toFloat(split(l.name, \",\")[1]))))) <" + leeway + "\n"
                + "MATCH (l)<-[:MENTIONS]-(book:Book)\n"
                + "RETURN l";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(new Book(record.get("l").asString()));
        }
        return books;
    }
}
