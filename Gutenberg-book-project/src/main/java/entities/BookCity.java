/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author Lasse
 */
public class BookCity {

    public String bookTitle, cityName;
    public float latitude, longtitude;

    public BookCity(String bookTitle, String cityName, float latitude, float longtitude) {
        this.bookTitle = bookTitle;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
    
    
}
