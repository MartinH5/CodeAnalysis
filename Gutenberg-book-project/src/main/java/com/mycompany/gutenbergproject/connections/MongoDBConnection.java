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
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author OpieOP
 */
public class MongoDBConnection {

    private static MongoClient mongo = null;

    public MongoDBConnection() {
    }

    public static MongoClient getMongoConnection() {
        try {
            if (mongo != null) {
                return mongo;
            } else {
                mongo = new MongoClient("localhost", 27017);

            }
        } catch (Exception e) {
            System.out.println("ERROR: Cannot connect to MongoDatabase, stacktrace: " + e.getMessage());
        } finally {
            return mongo;
        }
    }

    public static void main(String[] args) {
        MongoClient m = new MongoClient();
       
    }

    

}
