package org.library.controller;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.library.model.Borrow;
import org.library.model.Category;
import org.library.model.User;
import org.library.service.CategoryService;
import org.library.service.UserService;
import org.library.view.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * controller for user operations
 */
@Controller
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * get index page
     * @param session
     * @param model
     * @return
     */
    @GetMapping({"/", "/index", "/index.html", "/index.htm"})
    public String index(HttpSession session, Model model) {
        checkLogin(session, model);

        return "index";
    }

    /**
     * get index page
     * @param session
     * @param model
     * @return
     */
    @GetMapping({ "/rules"})
    public String rules(HttpSession session, Model model) {
        checkLogin(session, model);

        return "rules";
    }

    /**
     * get login page
     * @param session
     * @return
     */
    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/index";
        }
        return "login";
    }

    /**
     * get profile page
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/profile")
    public String getProfile(HttpSession session, Model model) {
        if (checkLogin(session, model) != null) {
            return "redirect:/login";
        }

        return "profile";
    }

    /**
     * post to add a new admin user
     * @param session
     * @param model
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/admin/new")
    public String addAdmin(HttpSession session, Model model,
            @RequestParam("username") String username, @RequestParam("password") String password) {
        User user = getLoginUser(session);
        if (user != null && user.isAdmin()) {
            User exist = userService.getUserByName(username);
            if (exist != null) {
                model.addAttribute("error", "User already exists");
            } else {
                User admin = new User();
                admin.setUsername(username);
                admin.setPassword(password);
                admin.setAdmin(true);
                admin = userService.addUser(admin);
                if (admin == null) {
                    model.addAttribute("error", "User add failed");
                }
            }

            return "redirect:/profile";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * get register page
     * @param session
     * @return
     */
    @GetMapping("/register")
    public String register(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/index";
        }
        return "register";
    }

    /**
     * logout
     * @param session
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/index";
    }

    /**
     * post to login
     * @param username
     * @param password
     * @param isAdmin
     * @param model
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("isAdmin") Optional<String> isAdmin,
            Model model,
            HttpSession session) {
        User user = userService.getUserByName(username);
        boolean adminBool = isAdmin.isPresent() && isAdmin.get().equals("on");
        if (user != null && user.getPassword().equals(password) && user.isAdmin() == adminBool) {
            session.setAttribute("user", user);
            return "redirect:/index";
        }

        model.addAttribute("error", "Invalid username or password.");
        return "login";
    }

    /**
     * post to update profile
     * @param id
     * @param password
     * @param fullName
     * @param phone
     * @param email
     * @param model
     * @param session
     * @return
     */
    @PostMapping("/profile")
    public String postProfile(@RequestParam("id") Integer id,
            @RequestParam("password") String password,
            @RequestParam("fullName") Optional<String> fullName,
            @RequestParam("phone") Optional<String> phone,
            @RequestParam("email") Optional<String> email,
            Model model,
            HttpSession session) {
        User user = userService.getUser(id);

        if (user != null) {
            user.setPassword(password);
            if (fullName.isPresent()) {
                user.setFullName(fullName.get());
            }
            if (phone.isPresent()) {
                user.setPhone(phone.get());
            }
            if (phone.isPresent()) {
                user.setPhone(phone.get());
            }
            if (email.isPresent()) {
                user.setEmail(email.get());
            }
            user = userService.addUser(user);
            if (user != null) {
                session.setAttribute("user", user);
            } else {
                model.addAttribute("error", "Add user failed");
            }
        } else {
            model.addAttribute("error", "User already exist.");
        }

        return "redirect:/profile";
    }

    /**
     * post to create a new user
     * @param username
     * @param password
     * @param fullName
     * @param email
     * @param phone
     * @param isAdmin
     * @param model
     * @param session
     * @return
     */
    @PostMapping("/register")
    public String processRegister(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("isAdmin") Optional<String> isAdmin,
            Model model,
            HttpSession session) {
        User user = userService.getUserByName(username);

        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setEmail(email);
            user.setAdmin(isAdmin.isPresent());
            user = userService.addUser(user);
            if(user != null) {
                session.setAttribute("user", user);
                return "redirect:/index";
            } else {
                model.addAttribute("error", "Add user failed");
            }
        } else {
            model.addAttribute("error", "User already exist.");
        }

        return "register";
    }
}
