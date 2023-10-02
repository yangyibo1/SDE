package org.library.service;

import java.util.List;
import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Category;
import org.library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for category
 */
@Service
public class CategoryService {
    private final CategoryRepository repo;

    @Autowired
    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    /**
     * get category by id
     * @param id
     * @return
     */
    public Category getCategory(Integer id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * delete category
     * @param category
     * @return
     */
    public boolean deleteCategory(Category category) {
        if (category == null) {
            return false;
        }
        return deleteCategory(category.getId());
    }

    /**
     * get all species
     * @return
     */
    public List<Category> getAllCategory() {
        return repo.findAll();
    }

    /**
     * delete category
     * @param id
     * @return
     */
    public boolean deleteCategory(Integer id) {
        if (id == null) {
            return false;
        }

        repo.deleteById(id);

        return true;
    }

    /**
     * get category by name
     * @param name
     * @return
     */
    public Category getCategoryByName(String name) {
        return repo.findByName(name).orElse(null);
    }

    /**
     * add category
     * @param category
     * @return
     */
    public Category addCategory(Category category) {
        if (category == null || category.getName() == null) {
            return null;
        }

        return repo.save(category);
    }

    /**
     * get book list by category
     * @param category
     * @return
     */
    public List<Book> getBookByCategory(Category category) {
        if (category == null) {
            return null;
        }

        Category exist = repo.findById(category.getId()).orElse(null);
        if (exist == null) {
            return null;
        }

        return exist.getBooks();
    }

    /**
     * only for testing
     */
    public void clearAll() {
        repo.deleteAll();
    }
}
