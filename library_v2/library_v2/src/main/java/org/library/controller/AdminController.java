package org.library.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.library.model.Category;
import org.library.model.User;
import org.library.service.CategoryService;
import org.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * controller for admin operations
 */
@Controller
public class AdminController extends AbstractController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * post to create a new category
     * @param session
     * @param model
     * @param category
     * @return
     */
    @PostMapping("/category/new")
    public String addCategory(HttpSession session, Model model,
            @RequestParam("category") String category) {
        User user = getLoginUser(session);
        if (user != null && user.isAdmin()) {
            Category exist = categoryService.getCategoryByName(category);
            if (exist != null) {
                model.addAttribute("error", "Category already exists");
            } else {
                Category newCategory = new Category();
                newCategory.setName(category);
                newCategory = categoryService.addCategory(newCategory);
                if (newCategory == null) {
                    model.addAttribute("error", "Category add failed");
                }
            }

            return "redirect:/profile";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * delete a category
     * @param session
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/category/delete")
    public String deleteCategory(HttpSession session, Model model,
            @RequestParam("id") Integer id) {
        User user = getLoginUser(session);
        if (user != null && user.isAdmin()) {
            Category exist = categoryService.getCategory(id);
            if (exist == null) {
                model.addAttribute("error", "Category not exists");
            } else {
                categoryService.deleteCategory(id);
            }

            return "redirect:/profile";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * update a category
     * @param session
     * @param model
     * @param id
     * @param category
     * @return
     */
    @PostMapping("/category/update")
    public String updateCategory(HttpSession session, Model model,
            @RequestParam("id") Integer id,
            @RequestParam("category") String category) {
        User user = getLoginUser(session);
        if (user != null && user.isAdmin()) {
            Category exist = categoryService.getCategory(id);
            Category newNameCategory = categoryService.getCategoryByName(category);
            if (exist == null) {
                model.addAttribute("error", "Category not exists");
            } else if (newNameCategory != null) {
                model.addAttribute("error", "Category with name " + category + " exists");
            } else {
                exist.setName(category);
                exist = categoryService.addCategory(exist);
                if (exist == null) {
                    model.addAttribute("error", "Category update failed");
                }
            }

            return "redirect:/profile";
        } else {
            return "redirect:/login";
        }
    }

}
