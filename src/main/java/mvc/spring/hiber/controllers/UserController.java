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

    // возвращает всех юзеров:
    @RequestMapping() // метод вызовится по адресу в классе "/users"
    public String allUsers(Model model) {
        model.addAttribute("alluserskey", userDAO.allUsers());
        return "usersview/allusers";

    }

    // возвращает страницу конкретного юзера по id:
    @GetMapping("/id")
    public String userById(@RequestParam int id, Model model) {
        model.addAttribute("userByIdkey", userDAO.userById(id));
        return "usersview/userById";
    }

    // возвращает HTML-форму для создания нового человека:
    @GetMapping("/new")
    public String viewForNewUser(@ModelAttribute("newkey") User user) {
        return "usersview/new";
    }

    // принимает POST-запрос, и ДОБАВЛЯЕТ нового юзера в БД с помощью DAO:
    @PostMapping()
    public String create(@ModelAttribute("createkey") User user) {
        userDAO.save(user);
        return "redirect:/users";
    }

    //  вызывается для подготовки редактирования юзера:
    @GetMapping("/edit/id")
    public String edit(@RequestParam int id, Model model) {
        model.addAttribute("editkey", userDAO.userById(id));
        return "usersview/edit";
    }

    // отправляет запрос в БД для записи отредактированного юзера:
    @PatchMapping("/id")
    public String update(@RequestParam int id, @ModelAttribute("updatekey") User user) {
        userDAO.update(id, user);
        return "redirect:/users";
    }

    // удаление человека по id на странице /users/id?id=
    @DeleteMapping("/id")
    public String delete(@RequestParam int id) {
        userDAO.delete(id);
        return "redirect:/users";
    }
}