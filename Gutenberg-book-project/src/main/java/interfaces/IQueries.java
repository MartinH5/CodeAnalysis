/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entities.Book;
import entities.City;
import java.util.List;

/**
 *
 * @author mart
 */
public interface IQueries {

    public List<Book> getAllBooks();

    public List<Book> getBooksMentioningCity(String city);

    //Get all Cities' coordinates - for plotting
    public List<City> getAllCities();

    //For plotting all Cities Related to Author
    public List<City> getCitiesRelatedtoAuthor(String Author);

    public List<Book> getBooksNearCoords(double lon, double lat, int leeway);

//    //Getting all books(names)
//    public List<String> getAllBooks();
//    //Getting all cities
//    public List<String> getAllCities();
//    //Get all cities mentioned in books by certain author
//    public List<String> getAuthorRelatedCities(String author);
//    //Author and author's books based off of city
//    public List<String> getAuthorBooksFromCities(String city);
//    //Cities from book
//    public List<String> getCitiesFromBook(String bookName);
//    
//    //  Get cities' longtitude / Latitude  
//    // public Collection?<float> getLongtitudeLatitudeFromCity(String city);    
}
