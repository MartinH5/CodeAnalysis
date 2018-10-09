package TDDTests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mycompany.gutenbergproject.connections.MongoDBConnection;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Opinator
 */
public class NoSQLIT {

    private MongoClient mongo;

    public NoSQLIT() {
    }

    @Before
    public void setUp() {
        mongo = getMongoConnection();
        //MySQLConnection sql = null;
        //Neo4jConnection; 
    }

    @Test
    public void getAllBooksMongoTest() {
        int bookCount;
        MongoClient myMongo = getMongoConnection();
        ArrayList<String> books = new ArrayList<>(); 
        DB db = getMongoDB(myMongo);
        DBCollection table = db.getCollection("books");
        DBCursor cursor = table.find();
        for (DBObject DBobject : cursor) {
            books.add(DBobject.get("Title").toString());
        }
        assertEquals(books.size(), 10);
    }
    
    
    public MongoClient getMongoConnection() {
        if (mongo != null) {
            return mongo;
        } else {
            mongo = new MongoClient("localhost", 27017);
            return mongo;
        }
    }
    
    public DB getMongoDB(MongoClient client) {
        DB db = client.getDB("bookstore");
        return db;
    }


//    //Match string with books and find equal or similar titles. 
//    @Test
//    public void getBooksByNameTest() {
//        
//    }
//    //Provide a list of all cities "supported"  
//    @Test
//    public void getAllCitiesTest() {
//        
//    }
//    //Get cities (or something else) within a specified range
//    @Test 
//    public void getNearbyCitiesTest(){
//    
//    }
//    //Get all mentioned cities from a book. 
//    @Test
//    public void getMentionedCitiesTest(){
//    
//    }
//    //Plot a list of city names on a map. 
//    @Test
//    public void plotCitiesTest(){
//    
//    }
    
    
    // Måden jeg har tænkt mig
    
//    @Test
//    @DisplayName("Returns all titles and authors from a book in a given city")
//    public void getBooksFromCityIT(){
////        mySQL test kode
//    }
//    
//    @Test
//    @DisplayName("Returns all cities mentioned in a books title and plots them onto a map")
//    public void getCityFromBookTitleIT(){
//        
//    }
//    
//    @Test
//    @DisplayName("Returns all books written by that author and plots mentioned cities in those books onto a map")
//    public void getBooksByAuthorIT(){
//        
//    }
//    
//    @Test
//    @DisplayName("Returns all books mentioning a city in vicinity of the given geolocation")
//    public void getBooksByGeolocationIT(){
//        
//    }
}
