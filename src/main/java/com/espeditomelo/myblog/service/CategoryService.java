package com.espeditomelo.myblog.service;

import com.espeditomelo.myblog.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    List<Category> findAllByNameAsc();
}
