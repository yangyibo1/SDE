package org.library;

import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.model.User;
import org.library.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    @Resource
    private UserService userService;
    private User user;


    @BeforeEach
    void setUp() {
        userService.clearAll();

        user = new User();
        user.setUsername("abc");
        user.setPassword("123");
        user.setFullName("Andy White");
        user.setEmail("user@a.com");
        user.setPhone("1234");
    }

    @Test
    public void testAddAndGetUser() throws Exception {
        User res = userService.addUser(user);
        Assert.assertNotNull(res);

        User exist = userService.getUser(res.getId());
        Assert.assertEquals(exist.getId(), res.getId());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User res = userService.addUser(user);
        Assert.assertNotNull(res);

        Assert.assertEquals("Andy White", user.getFullName());
        Assert.assertEquals("1234", user.getPhone());

        res.setFullName("Alice James");
        res.setPhone("1333");

        User user1 = userService.addUser(res);
        Assert.assertEquals(user1.getId(), res.getId());
        Assert.assertEquals("Alice James", user1.getFullName());
        Assert.assertEquals("1333", user1.getPhone());
    }

    @Test
    public void testDeleteUser() throws Exception {
        User res = userService.addUser(user);
        Assert.assertNotNull(res);

        userService.deleteUser(res.getId());

        Assert.assertNull(userService.getUser(res.getId()));
    }
}
