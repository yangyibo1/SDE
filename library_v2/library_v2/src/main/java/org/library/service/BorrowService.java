package org.library.service;

import java.util.Calendar;
import java.sql.Date;
import org.library.model.Book;
import org.library.model.Borrow;
import org.library.model.User;
import org.library.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for borrow
 */
@Service
public class BorrowService {
    private final BorrowRepository repo;

    @Autowired
    public BorrowService(BorrowRepository repo) {
        this.repo = repo;
    }

    /**
     * add a new borrow
     * @param user
     * @param book
     * @return
     */
    public Borrow addBorrow(User user, Book book) {
        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);

        Calendar calendar = Calendar.getInstance();
        borrow.setCheckoutDate(new Date(calendar.getTime().getTime()));
        calendar.add(Calendar.WEEK_OF_YEAR, 2);
        borrow.setDueDate(new Date(calendar.getTime().getTime()));
        borrow.setStatus("checkout");

        return repo.save(borrow);
    }

    /**
     * return a borrow
     * @param borrow
     * @return
     */
    public Borrow returnBorrow(Borrow borrow) {
        borrow.setStatus("returned");
        Calendar calendar = Calendar.getInstance();
        borrow.setReturnDate(new Date(calendar.getTime().getTime()));
        long dueTime = borrow.getDueDate().getTime();
        long days = (calendar.getTime().getTime() - dueTime) / 1000 / 86400;
        if (days > 3) {
            borrow.setDelayFee((double)(days - 3));
        }

        return repo.save(borrow);
    }

    /**
     * get borrow by id
     * @param id
     * @return
     */
    public Borrow getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * only for testing
     */
    public void clearAll() {
        repo.deleteAll();
    }
}
