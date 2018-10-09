/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenbergproject.connections;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

/**
 *
 * @author Micha
 */
public class Neo4jConnection {
    
    public static Driver driver;
    public static String url = "bolt://localhost:7687";
    public static String username = "neo4j";
    public static String password = "1234";
    
    public static Driver getConnection(){
        if (driver == null) {
            driver = GraphDatabase.driver(url, AuthTokens.basic(username, password));
        }
        return driver;
    }
    
    public static void closeNeoDriver() {
        driver.close();
        driver = null;
    }
    
    
    
//    public static void main(String[] args) {
//        Driver driver = GraphDatabase.driver( 
//                "bolt://localhost:7687", 
//                AuthTokens.basic( "neo4j", "1234" ) );
//        Session session = driver.session();
//
//        // Run a query matching all nodes
//        StatementResult result = session.run( 
//                "MATCH (s:Book)" +
//                "RETURN s.title AS title, s.author AS author");
//        
////        System.out.println("Title" + ", " + "Author");
//        while ( result.hasNext() ) {
//            Record record = result.next();
//            System.out.println( "Title: "+record.get("title").asString() + "\n" + 
//                                "Author: "+record.get("author").asString()+"\n");
//        }
//        session.close();
//        driver.close();
//    }
}
