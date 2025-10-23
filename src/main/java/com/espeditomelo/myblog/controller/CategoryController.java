package com.espeditomelo.myblog.controller;

import com.espeditomelo.myblog.model.Category;
import com.espeditomelo.myblog.model.User;
import com.espeditomelo.myblog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ModelAndView getCategories() {
        ModelAndView modelAndView = new ModelAndView("categories");
        List<Category> categories = categoryService.findAll();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @RequestMapping(value = "/editcategory/{id}", method = RequestMethod.GET)
    public ModelAndView getCategory(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView("categoryForm");
        Category category = categoryService.findById(id);
        modelAndView.addObject("category", category);
        return modelAndView;
    }

    @RequestMapping(value = "/editcategory/{id}", method = RequestMethod.POST)
    public ModelAndView saveEditedCategory(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("categoryForm");
            modelAndView.addObject("category", category);
            modelAndView.addObject("message", "All required fields must be completed");
            return modelAndView;
        }
        if (categoryService.findByName(category.getName()).isPresent()) {
            ModelAndView modelAndView = new ModelAndView("categoryForm");
            modelAndView.addObject("category", category);
            modelAndView.addObject("message", "The category name already registered");
            return modelAndView;
        }
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success", "Category edited successfully");
        return new ModelAndView("redirect:/categories");
    }

    @RequestMapping(value = "/newcategory", method = RequestMethod.GET)
    public ModelAndView getCategoryForm() {
        ModelAndView modelAndView = new ModelAndView("categoryForm");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @RequestMapping(value = "/newcategory", method = RequestMethod.POST)
    public ModelAndView saveCategory(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("categoryForm");
            modelAndView.addObject("category", category);
            modelAndView.addObject("message", "All required Fields must be completed");
            return modelAndView;
        }
        if (categoryService.findByName(category.getName()).isPresent()) {
            ModelAndView modelAndView = new ModelAndView("categoryForm");
            modelAndView.addObject("category", category);
            modelAndView.addObject("message", "The category name already registered");
            return modelAndView;
        }
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success", "Category added successfully");
        return new ModelAndView("redirect:/categories");
    }
}
