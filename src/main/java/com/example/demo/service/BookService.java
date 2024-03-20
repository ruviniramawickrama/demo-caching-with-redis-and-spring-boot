/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo.service;

import com.example.demo.model.Book;

public interface BookService {

    public Book createBook(Book book);

    public Book getBook(long id);

    public Book updateBook(Book book);

    public void deleteBook(long id);

}
