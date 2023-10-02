package org.library.service;

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

}
