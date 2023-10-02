package org.library.service;

import java.util.List;
import org.library.model.Book;
import org.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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

    /**
     * get book by id
     * @param id
     * @return book
     */
    public Book getBookById(Integer id) {
        if (id == null) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * update remain and borrow when borrow
     * @param book
     * @return
     */
    public Book checkoutBook(Book book) {
        book.setRemain(book.getRemain() - 1);
        book.setBorrow(book.getBorrow() + 1);

        return repo.save(book);
    }

    /**
     * update remain and borrow when return
     * @param book
     * @return
     */
    public Book returnBook(Book book) {
        book.setRemain(book.getRemain() + 1);
        book.setBorrow(book.getBorrow() - 1);

        return repo.save(book);
    }

    /**
     * add a new book
     * @param book
     * @return
     */
    public Book addBook(Book book) {
        return repo.save(book);
    }

    /**
     * update book info
     * @param book
     * @return
     */
    public Book updateBook(Book book) {
        if (book.getId() == null) {
            return null;
        }

        Book exist = getBookById(book.getId());
        if (exist == null) {
            return null;
        }

        exist.setTitle(book.getTitle());
        exist.setAuthor(book.getAuthor());
        exist.setCanBeBorrowed(book.isCanBeBorrowed());
        exist.setCategory(book.getCategory());
        exist.setDescription(book.getDescription());
        exist.setPublisherYear(book.getPublisherYear());
        exist.setLanguage(book.getLanguage());
        exist.setQuantity(book.getQuantity());
        exist.setRemain(book.getRemain());
        exist.setBorrow(book.getBorrow());

        return repo.save(exist);
    }

    /**
     * get all books and order by title
     * @return
     */
    public List<Book> getAllBooks() {
        return repo.findAll(Sort.by(Order.asc("title")));
    }

    /**
     * only for testing
     */
    public void clearAll() {
        repo.deleteAll();
    }
}
