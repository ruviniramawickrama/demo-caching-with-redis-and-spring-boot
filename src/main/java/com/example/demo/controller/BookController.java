/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@CacheConfig(cacheNames = "bookCache")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @GetMapping("/{id}")
    @Cacheable(keyGenerator = "bookCacheKeyGenerator")
    public Book getBook(@PathVariable long id) {
        return bookService.getBook(id);
    }

    @PutMapping("/{id}")
    @CachePut(keyGenerator = "bookCacheKeyGenerator")
    public Book updateBook(@PathVariable long id, @RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(keyGenerator = "bookCacheKeyGenerator", beforeInvocation = true, allEntries = true)
    public void deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
    }

}
