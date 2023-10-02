package org.library.service;

import java.util.List;
import org.library.model.Book;
import org.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for book
 */
@Service
public class BookService {
    private final BookRepository repo;

    @Autowired
    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    /**
     * get author by name
     * @param title
     * @return author
     */
    public List<Book> getBooksByTitle(String title) {
        return repo.findBooksByTitleContainsIgnoreCase(title);
    }
}
