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
public interface testInterface {
    
    
    public void getMentioningBooksWithAuthorTest(String cityname);
    
    public void mentionedCitiesTest(String booktitle);
    
    public void getBooksAndCitiesTest(String authorname);
    
    public void getBooksMentioningRange(float latitude, float longtitude, int leeway);
    
    
}
