/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package performanceTest;

/**
 *
 * @author OpieOP
 */
public class PerformanceMainTest {
 
    public static void main(String[] args) {
        System.out.println("Mongo Test Results: " + Mongo_performanceTest.MongoPerformance());
        System.out.println("MySQL Test Results: " + MYSQL_performanceTest.MySqlPerformance());
       //System.out.println("Neo4J Test Results: " + MYSQL_performanceTest.MySqlPerformance());
    }
    
}
