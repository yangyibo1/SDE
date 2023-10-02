package org.library.repository;

import java.util.Optional;
import org.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * repository for user
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * find user by username
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);
}
