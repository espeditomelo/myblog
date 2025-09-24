package com.espeditomelo.myblog.controller;

import com.espeditomelo.myblog.model.User;
import com.espeditomelo.myblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView("users");
        List<User> users = userService.findAllEnabled();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.GET)
    public ModelAndView getUserForm() {
        ModelAndView modelAndView = new ModelAndView("userForm");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public ModelAndView saveUser(@Valid User user, BindingResult  bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("userForm");
            modelAndView.addObject("user", user);
            modelAndView.addObject("message", "All required fields must be completed");
            return modelAndView;
        }
        if(userService.findByEmail(user.getEmail()).isPresent()){
            ModelAndView modelAndView = new ModelAndView("userForm");
            modelAndView.addObject("user", user);
            modelAndView.addObject("message", "The e-mail already registered");
            return modelAndView;
        }
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "User added successfully");
        return new ModelAndView("redirect:/users");
    }

    @RequestMapping(value = "/edituser/{id}", method = RequestMethod.GET)
    public ModelAndView getUser(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView("userForm");
        User user = userService.findById(id);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/edituser/{id}", method = RequestMethod.POST)
    public ModelAndView saveEditedUser(@Valid User user, BindingResult  bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("userForm");
            modelAndView.addObject("user", user);
            modelAndView.addObject("message", "All required fields must be completed");
            return modelAndView;
        }
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "User edited successfully");
        return new ModelAndView("redirect:/users");
    }

    @RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.GET)
    public ModelAndView logicalDeleteUser(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        userService.logicalDeleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        return new ModelAndView("redirect:/users");
    }

}
