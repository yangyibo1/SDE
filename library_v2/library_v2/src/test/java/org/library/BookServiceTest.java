package org.library;

import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.model.Book;
import org.library.model.User;
import org.library.service.BookService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookServiceTest {
    @Resource
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        bookService.clearAll();

        book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setLanguage("en");
        book.setPublisherYear(1988);
        book.setQuantity(10);
        book.setDescription("description");
        book.setRemain(10);

        book = bookService.addBook(book);
    }

    @Test
    public void testGetAndUpdate() throws Exception {
        Book book1 = bookService.getBookById(book.getId());
        Assert.assertNotNull(book1);
        Assert.assertEquals(book1.getId(), book.getId());

        book1.setTitle("title1");
        book1.setQuantity(100);
        book1 = bookService.updateBook(book1);

        Assert.assertEquals("title1", book1.getTitle());
        Assert.assertEquals(100, book1.getQuantity());
    }

    @Test
    public void testBorrowAndReturn() throws Exception {
        Assert.assertEquals(10, book.getRemain());
        Assert.assertEquals(0, book.getBorrow());

        book = bookService.checkoutBook(book);
        Assert.assertEquals(9, book.getRemain());
        Assert.assertEquals(1, book.getBorrow());

        book = bookService.returnBook(book);
        Assert.assertEquals(10, book.getRemain());
        Assert.assertEquals(0, book.getBorrow());
    }

}
