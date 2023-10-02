package org.library;

import jakarta.annotation.Resource;
import java.util.Date;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.model.Book;
import org.library.model.Borrow;
import org.library.model.User;
import org.library.service.BookService;
import org.library.service.BorrowService;
import org.library.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BorrowServiceTest {
    @Resource
    private BorrowService borrowService;
    @Resource
    private BookService bookService;
    @Resource
    private UserService userService;
    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        bookService.clearAll();
        borrowService.clearAll();
        userService.clearAll();

        book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setLanguage("en");
        book.setPublisherYear(1988);
        book.setQuantity(10);
        book.setDescription("description");
        book.setRemain(10);

        book = bookService.addBook(book);

        user = new User();
        user.setUsername("abc");
        user.setPassword("123");
        user.setFullName("Andy White");
        user.setEmail("user@a.com");
        user.setPhone("1234");

        user = userService.addUser(user);
    }

    @Test
    public void testAddBorrow() throws Exception {
        Borrow borrow = borrowService.addBorrow(user, book);
        Assert.assertNotNull(borrow);
        Assert.assertTrue(borrow.getId() > 0);
        Assert.assertEquals("checkout", borrow.getStatus());
        Assert.assertNotNull(borrow.getCheckoutDate().getTime());
    }

    @Test
    public void testReturnBorrow() throws Exception {
        Borrow borrow1 = borrowService.addBorrow(user, book);
        Assert.assertNotNull(borrow1);

        Borrow borrow = borrowService.returnBorrow(borrow1);
        Assert.assertNotNull(borrow);
        Assert.assertNotNull(borrow.getReturnDate());
        Assert.assertEquals("returned", borrow.getStatus());
    }
}
