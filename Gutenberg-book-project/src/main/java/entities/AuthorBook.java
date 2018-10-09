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
public class AuthorBook {

    public String author, book;

    public AuthorBook(String author, String book) {
        this.author = author;
        this.book = book;
    }

    public String getAuthor() {
        return author;
    }

    public String getBook() {
        return book;
    }

    @Override
    public String toString() {
        return author + " " + book;
    }

}
