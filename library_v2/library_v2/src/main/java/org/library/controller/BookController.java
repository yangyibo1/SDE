package org.library.controller;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.library.meta.BorrowRes;
import org.library.model.Book;
import org.library.model.Borrow;
import org.library.model.Category;
import org.library.model.User;
import org.library.service.BookService;
import org.library.service.BorrowService;
import org.library.service.CategoryService;
import org.library.view.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * controller for book operations
 */
@Controller
public class BookController extends AbstractController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    /**
     * query db and set category to model
     * @param model
     * @param categoryId
     * @return selected category
     */
    protected Category setCategoryModel(Model model, Integer categoryId) {
        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("categoryList", categoryList);

        Category allCategory = new Category();
        allCategory.setId(0);
        allCategory.setName("All");

        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }

        Category selected = null;
        if (categoryId != null) {
            Iterator<Category> iter = categoryList.iterator();
            while (iter.hasNext()) {
                Category item = iter.next();
                if (item.getId() == categoryId) {
                    iter.remove();
                    selected = item;
                    break;
                }
            }
        }

        if (selected == null) {
            categoryList.add(0, allCategory);
        } else {
            categoryList.add(0, selected);
            categoryList.add(allCategory);
        }

        return selected;
    }

    /**
     * view books
     * @param model
     * @param categoryId
     * @param session
     * @return
     */
    @GetMapping("/book")
    public String bookGet(Model model,
            @RequestParam("categoryId") Optional<Integer> categoryId,
            @RequestParam("title") Optional<String> title,
            @RequestParam("author") Optional<String> author,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("borrowRes") Optional<String> borrowRes,
            HttpSession session) {
        processBookRequest(categoryId.orElse(null), title.orElse(null),
                author.orElse(null), page.orElse(1), model, session);

        if (borrowRes.isPresent()) {
            setError(model, BorrowRes.valueOf(borrowRes.get()).getMessage());
        }

        return "book";
    }

    @PostMapping("/book")
    public String bookPost(HttpSession session, Model model,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("page") Integer page
    ) {
        processBookRequest(categoryId, title, author, page, model, session);
        return "book";
    }

    /**
     * process book request
     * @param categoryId
     * @param title
     * @param author
     * @param page
     * @param model
     * @param session
     */
    private void processBookRequest(
            Integer categoryId, String title, String author, Integer page, Model model, HttpSession session) {
        checkLogin(session, model);

        int currPage = page == null ? 1 : page;

        Category selectedCategory = setCategoryModel(model, categoryId);
        List<Book> bookList;
        if (selectedCategory != null && selectedCategory.getId() > 0) {
            bookList = selectedCategory.getBooks() == null ? new ArrayList() : selectedCategory.getBooks();
        } else {
            bookList = bookService.getAllBooks();
        }

        if (title != null && !title.isEmpty()) {
            model.addAttribute("title", title);
        } else {
            model.addAttribute("title", "");
        }
        if (author != null && !author.isEmpty()) {
            model.addAttribute("author", author);
        } else {
            model.addAttribute("author", "");
        }

        filterBooksByTitle(bookList, title, author);

        new Pager(bookList, currPage, model);
    }

    /**
     * change book to enable or disable
     * @param session
     * @param id
     * @return
     */
    @GetMapping("/book/changeEnable")
    public String changeEnable(HttpSession session, @RequestParam("id") Integer id, @RequestParam("enable") Integer enable) {
        User user = getLoginUser(session);
        if (user == null || !user.isAdmin()) {
            return "redirect:/login";
        }

        Book book = bookService.getBookById(id);
        if (book != null) {
            boolean enabled = enable == 1;
            book.setCanBeBorrowed(enabled);
            bookService.updateBook(book);
        }

        return "redirect:/report";
    }

    /**
     * filter books
     * @param bookList
     * @param title
     * @param author
     */
    private void filterBooksByTitle(List<Book> bookList, String title, String author) {
        title = title == null ? null : title.toLowerCase();
        author = author == null ? null : author.toLowerCase();
        if (title == null && author == null) {
            return;
        }

        Iterator<Book> iterator = bookList.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (!book.getTitle().toLowerCase().contains(title)
                    || !book.getAuthor().toLowerCase().contains(author)) {
                iterator.remove();
            }
        }
    }
}
