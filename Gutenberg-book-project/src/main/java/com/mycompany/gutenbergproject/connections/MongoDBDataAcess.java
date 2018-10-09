/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenbergproject.connections;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import entities.Book;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bson.Document;

/**
 *
 * @author OpieOP
 */
public class MongoDBDataAcess {

    private MongoDBConnection connection = null;
    private static MongoClient mongo = null;

    public MongoDBDataAcess() {
        this.connection = new MongoDBConnection();
        this.mongo = connection.getMongoConnection();
    }

    public static void main(String[] args) {
        MongoDBDataAcess dbAcess = new MongoDBDataAcess();
        ArrayList<Book> books = getBooksMentioningNearbyCities(-99.0815f, 40.6995f, 10000);
        for (Book book : books) {
            System.out.println("  Title: " + book.getTitle());
        }
       // createTable2();

//        ArrayList<Document> myBooks = getAuthorMentionedCities("Orr, Lyndon");
//        for (Document doc : myBooks) {   
//            String[] numbers = doc.toString().replaceAll( "[^0-9.,]", "" ).split(",");
//            
//            System.out.println("[" + doc.getString("name") + "," + numbers[5] + "," + numbers[4] + "]");
//        }
        MongoDatabase db = mongo.getDatabase("tester5");
        MongoIterable collections = db.listCollectionNames();
        for (Object collection : collections) {
            System.out.println(collection);
        }

    }

    public static ArrayList<Document> getCityMentionsFromBook(int bookId) {
        ArrayList<Integer> mentions = new ArrayList();
        ArrayList<Document> cities = new ArrayList<>();
        MongoDatabase db = mongo.getDatabase("tester5");
        MongoCollection<Document> coll = db.getCollection("bookCityRelations");
        BasicDBObject query = new BasicDBObject("book_id", bookId);
        FindIterable<Document> docs = coll.find(query);
        for (Document doc : docs) {
            mentions.add(doc.getInteger("city_id"));
        }
        BasicDBObject query2 = new BasicDBObject();
        query2.put("id", new BasicDBObject("$in", mentions));
        MongoCollection<Document> coll2 = db.getCollection("cities");
        FindIterable<Document> docs2 = coll2.find(query2);
        for (Document doc : docs2) {
            cities.add(doc);
        }
        return cities;
    }

    public static ArrayList<Document> getBooksByAuthor(int authorId) {
        ArrayList<Document> booksFromAuthor = new ArrayList();
        ArrayList<Integer> authorsRelations = new ArrayList();
        MongoDatabase db = mongo.getDatabase("tester5");
        MongoCollection<Document> coll = db.getCollection("authorBookRelations");
        BasicDBObject query = new BasicDBObject("author_id", authorId);
        FindIterable<Document> docs = coll.find(query);
        for (Document doc : docs) {
            authorsRelations.add(doc.getInteger("book_id"));
        }
        BasicDBObject query2 = new BasicDBObject();
        query2.put("id", new BasicDBObject("$in", authorsRelations));
        MongoCollection<Document> coll2 = db.getCollection("books");
        FindIterable<Document> docs2 = coll2.find(query2);
        for (Document doc : docs2) {
            System.out.println(doc.toJson());
            booksFromAuthor.add(doc);
        }
        return booksFromAuthor;
    }

    public static ArrayList<Document> getAuthorMentionedCities(String authorName) {
        ArrayList<Document> CitiesMentioned = new ArrayList();
        Set<Integer> authorCities = new HashSet();
        MongoDatabase db = mongo.getDatabase("tester5");
        MongoCollection<Document> coll = db.getCollection("allRelations");
        BasicDBObject query = new BasicDBObject("author_id", getIntegerId(authorName, db.getCollection("authors2")));
        FindIterable<Document> docs = coll.find(query);
        for (Document doc : docs) {
            authorCities.add(doc.getInteger("city_id"));
        }
        BasicDBObject query2 = new BasicDBObject();
        query2.put("id", new BasicDBObject("$in", authorCities));
        MongoCollection<Document> coll2 = db.getCollection("cities");
        FindIterable<Document> docs2 = coll2.find(query2);
        System.out.println("First doc: " + docs2.first());
        for (Document doc : docs2) {
            CitiesMentioned.add(doc);
        }
        //DBObject query = QueryBuilder.start("id").in(new String[] {"foo", "bar"}).get();        
        return CitiesMentioned;
    }

    public static ArrayList<Book> getBooksMentioningNearbyCities(float longtitude, float lattitude, int leeway) {
        ArrayList<Book> nearbyBooks = new ArrayList<>();
        Set<Integer> nearbyCities = new HashSet<>();
        Set<Integer> nearbyBookIds = new HashSet<>();
        MongoDatabase db = mongo.getDatabase("tester5");
        MongoCollection<Document> coll = db.getCollection("cities");
        float[] cords = {longtitude, lattitude};

        BasicDBObject queryOnlyFind = new BasicDBObject("location",
                new BasicDBObject("$nearSphere", new BasicDBObject("type", "Point").append("coordinates", cords)).append("$maxDistance", leeway));

        FindIterable<Document> docs = coll.find(queryOnlyFind);
        for (Document doc : docs) {
            nearbyCities.add(doc.getInteger("id"));
        }
        MongoCollection<Document> coll2 = db.getCollection("bookCityRelations");
        BasicDBObject query2 = new BasicDBObject();
        query2.put("id", new BasicDBObject("$in", nearbyCities));
        FindIterable<Document> docs2 = coll2.find(query2);
        for (Document doc : docs2) {
            nearbyBookIds.add(doc.getInteger("book_id"));
        }
        MongoCollection<Document> coll3 = db.getCollection("books");
        BasicDBObject query3 = new BasicDBObject();
        query3.put("id", new BasicDBObject("$in", nearbyBookIds));
        FindIterable<Document> docs3 = coll3.find(query3);
        for (Document doc : docs3) {
            nearbyBooks.add(new Book(doc.getString("title")));
        }
        return nearbyBooks;
    }

    public static ArrayList<Document> getAllBookNames(String collectionName) {
        ArrayList<Document> books = new ArrayList<>();
        MongoDatabase db = mongo.getDatabase("tester5");
        MongoCollection<Document> collection = db.getCollection(collectionName);
        FindIterable<Document> docs = collection.find();
        System.out.println("This is a test btw: -----------" + docs.first().toJson());
        for (Document doc : docs) {
            books.add(doc);
        }
        return books;
    }

    public static ArrayList<String> getAllCityNames(String collectionName) {
        ArrayList<String> cities = new ArrayList<>();
        MongoDatabase db = mongo.getDatabase("tester5");
        MongoCollection<Document> collection = db.getCollection(collectionName);
        FindIterable<Document> docs = collection.find();

        for (Document doc : docs) {
            cities.add(doc.getString("long").toString());
        }
        return cities;
    }

    private static Integer getIntegerId(String name, MongoCollection<Document> mongoColl) {
        BasicDBObject query = new BasicDBObject("name", name);
        FindIterable<Document> docs = mongoColl.find(query);
        System.out.println("Giv mig et hej");
        for (Document doc : docs) {
            System.out.println("hej");
        }
        if (docs.first().getInteger("id") != null) {
            return docs.first().getInteger("id");
        }
        return null;
    }

    public String getBookInfo(String bookName) {
        // DB db = getMongoDB(mongo);
        // DBCollection table = db.getCollection("books");
        //    return table.find("");
        return null;
    }

    
    public static void createTable2() {
        System.out.println("Kører Klasse");
        MongoDatabase db = mongo.getDatabase("tester5");
        List<Document> books = getAllBookNames("books");
        List<Document> newBooks = new ArrayList();
        MongoCollection<Document> collection = db.getCollection("authorBookRelations");
        MongoCollection<Document> collection2 = db.getCollection("authors"); 
        BasicDBObject bdo;
        Integer tempAuthorId;
        String tempAuthorName;
        for (Document book : books) {
            bdo = new BasicDBObject ("book_id",book.getInteger("id")); 
            tempAuthorId = collection.find(bdo).first().getInteger("author_id");
            System.out.println("myBdo: " + bdo);
            bdo = new BasicDBObject("id",tempAuthorId);
            System.out.println("myBdo: " + bdo);
            tempAuthorName = collection.find(bdo).first().getString("name");
            
            book.append("author", tempAuthorName);
            newBooks.add(book);
        }
        MongoCollection<Document> collection4 = db.getCollection("bookFinal");
        collection4.insertMany(newBooks);
    }

    public static void createTable() {

        MongoDatabase db = mongo.getDatabase("tester5");
        ArrayList<Document> books = new ArrayList<>();
        ArrayList<Document> relations = new ArrayList<>();
        MongoCollection<Document> collection = db.getCollection("books");
        FindIterable<Document> docs = collection.find();

        for (Document doc : docs) {
            books.add(doc);
        }

        MongoCollection<Document> collection2 = db.getCollection("authorBooksRelations");
        FindIterable<Document> docs2 = collection.find();

        for (Document doc : docs2) {
            relations.add(doc);
        }

        MongoCollection<Document> collection3 = db.getCollection("authors");
        FindIterable<Document> docs3 = collection.find();
        for (Document book : books) {
            int bookId = book.getInteger("id");
            BasicDBObject tempQuery = new BasicDBObject("book_id", bookId);
            FindIterable<Document> tempIds = collection2.find(tempQuery);
            ArrayList<Integer> indexes = new ArrayList();
            for (Document document : tempIds) {
                document.getInteger("author_id");
            }
            BasicDBObject tempQuery2 = new BasicDBObject();
            tempQuery2.put("id", new BasicDBObject("$in", tempIds));
            FindIterable<Document> tempDocs = collection3.find(tempQuery2);
            for (Document tempDoc : tempDocs) {
                book.append("author", tempDoc);
            }
        }
        MongoCollection<Document> collection4 = db.getCollection("bookFinal");
        collection4.insertMany(books);
    }

}
