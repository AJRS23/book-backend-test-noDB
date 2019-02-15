package com.example.demo.core.service;

import com.example.demo.core.dao.BookDao;
import com.example.demo.model.Book;
import com.example.demo.support.exceptions.BookAlreadyExistsException;
import com.example.demo.support.exceptions.BookNameFieldRequiredException;
import com.example.demo.support.exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class BookServiceImpl implements BookService {

    private BookDao bookDao;

    public BookServiceImpl() {
    }

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book createBook(String name, String author) throws Exception {
        if (name == null) {
            throw new BookNameFieldRequiredException();
        }
        Book book = this.bookDao.getBookByNameAndAuthor(name, author);
        if (book != null) {
            throw new BookAlreadyExistsException();
        }
        book = new Book();
        book.setId(UUID.randomUUID().toString());
        book.setName(name);
        book.setAuthor(author);
        book = this.bookDao.createBook(book);
        return book;
    }

    @Override
    public Book updateBook(String id, String name, String author) throws Exception {
        if (name == null) {
            throw new BookNameFieldRequiredException();
        }
        Book book = this.bookDao.getBookByNameAndAuthor(name, author);
        if (book != null && !book.getId().equals(id)) {
            throw new BookAlreadyExistsException();
        }
        book = this.bookDao.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException();
        }
        book.setName(name);
        book.setAuthor(author);
        book = this.bookDao.updateBook(book);
        return book;
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books = this.bookDao.getBooks();
        return books;
    }

    @Override
    public Book getBook(String id) throws Exception {
        Book book = this.bookDao.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return book;
    }

    @Override
    public void deleteBook(String id) throws Exception {
        List<Book> books = this.bookDao.getBooks();
        Book bookToDelete = null;
        for (Book book : books) {
            if (book.getId().equals(id)) {
                bookToDelete = book;
                break;
            }
        }
        if (bookToDelete != null) {
            this.bookDao.deleteBook(bookToDelete);
        }
        else {
            throw new BookNotFoundException();
        }
    }
}
