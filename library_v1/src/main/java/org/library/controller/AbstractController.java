package org.library.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.library.model.User;
import org.springframework.ui.Model;

/**
 * abstract controller for some common methods 
 */
public abstract class AbstractController {
    /**
     * get user if login
     * @param session
     * @return
     */
    public Optional<User> getLoginUser(HttpSession session) {
        User user = null;
        Object tmp = session.getAttribute("user");
        if (tmp != null) {
            user = (User) tmp;
        }

        return Optional.ofNullable(user);
    }

    /**
     * check if login and set user to model
     * @param session
     * @param model
     * @return
     */
    public String checkLogin(HttpSession session, Model model) {
        return checkLogin(session, model, false);
    }

    /**
     * check if login and set user
     * @param session
     * @param model
     * @param requiredAdmin
     * @return
     */
    public String checkLogin(HttpSession session, Model model, boolean requiredAdmin) {
        Optional<User> user = getLoginUser(session);
        if (user.isPresent()) {
            if (requiredAdmin && !user.get().isAdmin()) {
                return "redirect:/index";
            }

            model.addAttribute("user", user);
            return null;
        }

        return "redirect:/login";
    }
}
