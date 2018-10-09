/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TDDTests;

import com.mycompany.gutenbergproject.connections.Neo4jConnection;
import entities.AuthorBook;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

/**
 *
 * @author Micha
 */
public class Neo4jIT {

//    @Ignore
    @Test
    @DisplayName("Test connection, by getting all books")
    public void getAllBooks() {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<String> books = new ArrayList<>();
        int expectedResult = 36597;

        Session session = driver.session();
        String query = "MATCH (s:Book)"
                + "RETURN s.title AS title";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(record.get("title").asString());
        }
        int actualResult = books.size();

        assertThat(books.isEmpty(), is(false));
        assertThat(books.get(1), equalTo("`Abdul-Bahás Tablet to Dr. Forel"));
        assertEquals(expectedResult, actualResult);
    }

//    @Ignore
    @Test
    @DisplayName("Returns all titles and authors from a book in a given city")
    public void getBooksFromCityIT() {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<AuthorBook> books = new ArrayList<>();
        int expectedResult = 32169;
        String name = "\"'London'\"";
        String index = "Wylders Hand" + " " + "Le Fanu, Joseph Sheridan";

        Session session = driver.session();
        String query = "MATCH ({name:" + name + "})<-[:MENTIONS]-(book:Book)<-[:WRITTEN]-(author:Author)\n"
                + "RETURN book.title AS title, author.name AS name";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(new AuthorBook(record.get("title").asString(), record.get("name").asString()));
        }
        int actualResult = books.size();

//        System.out.println(books);
        assertThat(books.isEmpty(), is(false));
        assertThat(books.containsAll(books), is(true));
//        assertThat(books.get(7), equalTo(index));
        assertEquals(expectedResult, actualResult);
    }

//    @Ignore
    @Test
    @DisplayName("Returns all cities mentioned in a books title and plots them onto a map")
    public void getCityFromBookTitleIT() {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<String> books = new ArrayList<>();
        int expectedResult = 28;
        String title = "\"Pocahontas, A Poem\"";
        String index = "'Bay'" + " " + "14.1837" + " " + "121.286";

        Session session = driver.session();
        String query = "MATCH ({title:" + title + "})-[:MENTIONS]->(city:City)\n"
                + "RETURN city.name AS name, city.latitude AS latitude, city.longitude AS longitude";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(record.get("name").asString() + " " + record.get("latitude").asString() + " " + record.get("longitude").asString());
        }
        int actualResult = books.size();

        assertThat(books.isEmpty(), is(false));
        assertThat(books.containsAll(books), is(true));
        assertThat(books.get(10), equalTo(index));
        assertEquals(expectedResult, actualResult);
    }

//    @Ignore
    @Test
    @DisplayName("Returns all books written by that author and plots mentioned cities in those books onto a map")
    public void getBooksByAuthorIT() {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<String> books = new ArrayList<>();
        int expectedResult = 387;
        String name = "\"Adams, Charles Francis\"";
        String index = "\"Imperialism\" and \"The Tracks of Our Forefathers\"" + " " + "'Venezuela'" + " " + "21.7375" + " " + "-78.7934";

        Session session = driver.session();
        String query = "MATCH ({name:" + name + "})-[:WRITTEN]->(book:Book)-[:MENTIONS]->(city:City)\n"
                + "RETURN book.title AS title, city.name AS name, city.latitude AS latitude, city.longitude AS longitude";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(record.get("title").asString() + " " + record.get("name").asString() + " " + record.get("latitude").asString() + " " + record.get("longitude").asString());
        }
        int actualResult = books.size();

        assertThat(books.isEmpty(), is(false));
        assertThat(books.containsAll(books), is(true));
        assertThat(books.get(2), equalTo(index));
        assertEquals(expectedResult, actualResult);
    }

//    @Ignore
    @Test
    @DisplayName("Returns all books mentioning a city in vicinity of the given geolocation")
    public void getBooksByGeolocationIT() {
        Driver driver = Neo4jConnection.getConnection();
        ArrayList<String> books = new ArrayList<>();
        int expectedResult = 0;
        String latitude = "53.7939";
        String longitude = "-1.75206";
        int distance = 20;
        String index = "";

        Session session = driver.session();
        String query = "WITH " + latitude + " AS lat, " + longitude + " AS lon\n"
                + "MATCH (l:City) \n"
                + "WHERE 2 * 6371 * asin(sqrt(haversin(radians(lat - toFloat(split(l.name, \",\")[0])))+ cos(radians(lat))* cos(radians(toFloat(split(l.name, \",\")[0])))* haversin(radians(lon - toFloat(split(l.name, \",\")[1]))))) <" + distance + "\n"
                + "MATCH (l)<-[:MENTIONS]-(book:Book)\n"
                + "RETURN l";

        StatementResult result = session.run(query);

        while (result.hasNext()) {
            Record record = result.next();
            books.add(record.get("name").asString());
        }
        int actualResult = books.size();

        assertThat(books.isEmpty(), is(true));
        assertThat(books.containsAll(books), is(true));
//        assertThat(books.get(0), equalto(index));
        assertEquals(expectedResult, actualResult);
    }
}
