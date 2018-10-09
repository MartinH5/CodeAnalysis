/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenbergproject.connections;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author OpieOP
 */
public class MongoDBImportData {

    private MongoDBConnection connection = null;
    private static MongoClient mongo = null;

    public MongoDBImportData() {
        this.connection = new MongoDBConnection();
        this.mongo = connection.getMongoConnection();
    }

    public static void main(String[] args) throws IOException {

        MongoDBImportData mongoImport = new MongoDBImportData();
        importData("C:\\Users\\OpieOP\\Downloads\\temp\\Actual Data\\gutenberg_csv\\Test Csver\\author_t.csv", "authors");
    }

    public static void importData(String csvFileName, String collectionName) throws IOException {
        System.out.println("Doing Data Import :-----------------------");
        MongoDatabase db = mongo.getDatabase("test3");
        MongoCollection<Document> collection = db.getCollection(collectionName);
        System.out.println("Importing data ----------------");
        List<String[]> dbRecords = readData(csvFileName);
        System.out.println("I ended up with this many records:.------------- " + dbRecords.size());
        for (String[] dbRecord : dbRecords) {
            for (String string : dbRecord) {
                System.out.println("This is my String: " + string);
            }
        }
    }

    public static List<String[]> readData(String csvFileName) throws IOException {
        int count = -1;     
        List<String[]> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                content.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            //Some error logging
        }
        return content;
    }
}
