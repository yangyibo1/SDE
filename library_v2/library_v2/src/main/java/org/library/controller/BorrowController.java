package org.library.controller;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.library.meta.BorrowRes;
import org.library.meta.ReturnRes;
import org.library.model.Book;
import org.library.model.Borrow;
import org.library.model.User;
import org.library.service.BookService;
import org.library.service.BorrowService;
import org.library.service.UserService;
import org.library.view.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * controller for borrow operations
 */
@Controller
public class BorrowController extends AbstractController {
    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    /**
     * get borrow page
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/myBorrow")
    public String borrowGet(HttpSession session, Model model,
            @RequestParam("status") Optional<String> status,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("returnRes") Optional<String> returnRes) {

        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        if (returnRes.isPresent()) {
            setError(model, ReturnRes.valueOf(returnRes.get()).getMessage());
        }
        model.addAttribute("user", user);

        processBorrowRequest(user, status.orElse(null), page.orElse(1), model);

        return "borrow";
    }

    /**
     * post borrow request
     * @param session
     * @param model
     * @param status
     * @param page
     * @return
     */
    @PostMapping("/myBorrow")
    public String borrowPost(HttpSession session, Model model,
            @RequestParam("status") String status,
            @RequestParam("page") Integer page
    ) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        processBorrowRequest(user, status, page, model);

        return "borrow";
    }

    /**
     * process borrow get or post
     * @param user
     * @param status
     * @param page
     * @param model
     */
    private void processBorrowRequest(User user, String status, Integer page, Model model) {
        List<String> statusList = new ArrayList<String>();
        statusList.addAll(Arrays.asList("All", "checkout", "returned"));
        if (status != null) {
            statusList.remove(status);
            statusList.add(0, status);
        }

        model.addAttribute("statusList", statusList);

        user = userService.getUser(user.getId());
        List<Borrow> borrowList = user.getBorrows() == null ? new ArrayList() : user.getBorrows();
        if (status != null && !status.equals("All")) {
            Iterator<Borrow> iterator = borrowList.iterator();
            while (iterator.hasNext()) {
                Borrow borrow = iterator.next();
                if (!borrow.getStatus().equals(status)) {
                    iterator.remove();
                }
            }
        }
        Collections.sort(borrowList, (o1, o2) -> o2.getCheckoutDate().compareTo(o1.getCheckoutDate()));

        Pager<Borrow> pager = new Pager(borrowList, page, model);
    }

    /**
     * borrow one book
     * @param bookId
     * @param session
     * @return
     */
    @GetMapping("/borrow")
    public String borrowBook(@RequestParam("id") Optional<Integer> bookId,
            HttpSession session) {
        User user = getLoginUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        Book book = bookService.getBookById(bookId.orElse(null));

        if (book == null) {
            return "redirect:/book?borrowRes=" + BorrowRes.BOOK_NOT_FOUND;
        }

        user = userService.getUser(user.getId());
        if (user.getBorrows() != null) {
            int borrowCnt = 0;
            boolean hasOverdue = false;
            for (Borrow borrow: user.getBorrows()) {
                if (borrow.getStatus().equals("checkout")) {
                    if (borrow.getDueDate().before(new Date())) {
                        hasOverdue = true;
                        break;
                    }
                    if (borrow.getId() == book.getId()) {
                        return "redirect:/book?borrowRes=" + BorrowRes.ALREADY_BORROWED;
                    }
                    borrowCnt++;
                }
            }

            if (hasOverdue) {
                return "redirect:/book?borrowRes=" + BorrowRes.OVERDUE;
            }

            if (borrowCnt > 0) {
                return "redirect:/book?borrowRes=" + BorrowRes.BORROW_OVER_LIMIT;
            }
        }

        if (book.getRemain() == 0) {
            return "redirect:/book?borrowRes=" + BorrowRes.NO_COPY;
        }

        bookService.checkoutBook(book);

        Borrow borrow = borrowService.addBorrow(user, book);
        if (borrow == null) {
            return "redirect:/book?borrowRes=" + BorrowRes.SYSTEM_ERROR;
        } else {
            return "redirect:/book?borrowRes=" + BorrowRes.SUCCESS;
        }
    }

    /**
     * return one book
     * @param session
     * @param model
     * @param borrowId
     * @return
     */
    @GetMapping("/return")
    public String returnBook(HttpSession session, Model model,
            @RequestParam("id") Optional<Integer> borrowId) {
        User user = getLoginUser(session);
        if (user == null || !borrowId.isPresent()) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        Borrow borrow = borrowService.getById(borrowId.get());
        if (borrow == null) {
            return "redirect:/myBorrow?returnRes=" + ReturnRes.BORROW_NOT_FOUND;
        }

        if (borrow.getStatus() == "returned") {
            return "redirect:/myBorrow?returnRes=" + ReturnRes.ALREADY_RETURNED;
        }

        bookService.returnBook(borrow.getBook());
        borrowService.returnBorrow(borrow);

        return "redirect:/myBorrow?returnRes=" + ReturnRes.SUCCESS;
    }
}
