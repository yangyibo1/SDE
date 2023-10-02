package org.library.repository;

import java.util.Optional;
import org.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for categories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * find category by name
     * @param name
     * @return
     */
    Optional<Category> findByName(String name);

}
