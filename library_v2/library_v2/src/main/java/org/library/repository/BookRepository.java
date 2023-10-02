package org.library.repository;

import java.util.List;
import org.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for book
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    /**
     * find books by title
     * @param title
     * @return
     */
    List<Book> findBooksByTitleContainsIgnoreCase(String title);
}
