package org.library.service;

import org.library.model.Author;
import org.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for author
 */
@Service
public class AuthorService {
    private final AuthorRepository repo;

    @Autowired
    public AuthorService(AuthorRepository repo) {
        this.repo = repo;
    }

    /**
     * get author by name
     * @param name
     * @return author
     */
    public Author getAuthorByName(String name) {
        return repo.findByName(name).orElse(null);
    }
}
