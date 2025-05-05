package mvc.spring.hiber.controllers;

import mvc.spring.hiber.dao.UserDAO;
import mvc.spring.hiber.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @RequestMapping()
    public String allUsers(Model model) {
        model.addAttribute("alluserskey", userDAO.allUsers());
        return "usersview/allusers";

    }

    @GetMapping("/id")
    public String userById(@RequestParam int id, Model model) {
        model.addAttribute("userByIdkey", userDAO.userById(id));
        return "usersview/userById";
    }

    @GetMapping("/new")
    public String viewForNewUser(@ModelAttribute("newkey") User user) {
        return "usersview/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("createkey") User user) {
        userDAO.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/id")
    public String edit(@RequestParam int id, Model model) {
        model.addAttribute("editkey", userDAO.userById(id));
        return "usersview/edit";
    }

    @PatchMapping("/id")
    public String update(@RequestParam int id, @ModelAttribute("updatekey") User user) {
        userDAO.update(id, user);
        return "redirect:/users";
    }

    @DeleteMapping("/id")
    public String delete(@RequestParam int id) {
        userDAO.delete(id);
        return "redirect:/users";
    }
}