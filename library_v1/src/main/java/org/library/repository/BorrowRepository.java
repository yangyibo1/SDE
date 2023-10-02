package org.library.repository;

import java.util.List;
import org.library.model.Borrow;
import org.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for borrow
 */
@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {

    /**
     * find borrows by user
     * @param userId
     * @return borrowed list
     */
    List<Borrow> findByUserId(Integer userId);

    /**
     * find borrows by book
     * @param bookId
     * @return borrowed list
     */
    List<Borrow> findByBookId(Integer bookId);
}
