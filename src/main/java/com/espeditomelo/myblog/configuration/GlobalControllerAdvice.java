package com.espeditomelo.myblog.configuration;

import com.espeditomelo.myblog.model.Category;
import com.espeditomelo.myblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("allCategories")
    public List<Category> getAllCategories(){
        try {
            List<Category> categories = categoryService.findAllByNameAsc();
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading categories ");
            return List.of();
        }
    }
}
