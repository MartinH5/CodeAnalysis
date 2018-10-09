/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.List;

/**
 *
 * @author OpieOP
 */
public class BookCities {
    private String bookName;
    private List<String> cities;
    
    public BookCities(String bookName, List<String> cities) {
        this.bookName = bookName;
        this.cities = cities;
    }
    
}
