/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package performanceTest;

import java.util.ArrayList;
import org.junit.Before;
import queries.QueryInterface;

/**
 *
 * @author OpieOP
 */
public class Neo4J_performanceTest {

    private final String[] citySet = {"'Sar-e Pul'", "'Szczecin'", "'New York City'", "'London'", "'Copenhagen'"};
    private final String[] titleSet = {"Charlotte Brontë and Her Circle", "I Married a Ranger, A Short Method Of Prayer", "In the Track of the Bookworm", "Much Ado About Something", "Bimbi, Stories for Children"};
    private final String[] authorSet = {"Abbott, Edwin Abbott", "Karasowski, Maurycy", "Taylor, Robert Bruce", "Sherman, Frederic, Mrs.", "Terry, Dorothy"};
    private final float[][] coordinateSet = {{51f, 0f}, {36.2154f, 65.9325f}, {53.4289f, 14.553f}, {40.7143f, -74.006f}, {55.6759f, 12.5655f}};
    private static long start;
    private static long end;

    public ArrayList<Double> Neo4JPerformance() {
        ArrayList<Double> results = new ArrayList();
        start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            getMentioningBooksWithAuthorTest(citySet[i]);
        }
        end = System.currentTimeMillis();
        results.add((double) (end - start) / 1000.00);

        start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            mentionedCitiesTest(titleSet[i]);
        }
        end = System.currentTimeMillis();
        results.add((double) (end - start) / 1000.00);

        start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            getBooksAndCitiesTest(authorSet[i]);
        }
        end = System.currentTimeMillis();
        results.add((double) (end - start) / 1000.00);

        start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            getBooksMentioningRange(coordinateSet[i][0], coordinateSet[i][1], 15000);
        }
        end = System.currentTimeMillis();
        results.add((double) (end - start) / 1000.00);

        return results;

    }

    @Before
    public void setUp() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public static void getMentioningBooksWithAuthorTest(String cityname) {
        QueryInterface.getMentioningBooksWithAuthors(cityname, QueryInterface.DBChoice.DB_NEO4J);
    }

    public static void mentionedCitiesTest(String booktitle) {
        QueryInterface.mentionedCities(booktitle, QueryInterface.DBChoice.DB_NEO4J);
    }

    public static void getBooksAndCitiesTest(String authorname) {
        QueryInterface.getBooksAndCities(authorname, QueryInterface.DBChoice.DB_NEO4J);
    }

    public static void getBooksMentioningRange(float latitude, float longtitude, int leeway) {
        QueryInterface.getBooksMentioningRange(latitude, longtitude, leeway, QueryInterface.DBChoice.DB_NEO4J);
    }
}
