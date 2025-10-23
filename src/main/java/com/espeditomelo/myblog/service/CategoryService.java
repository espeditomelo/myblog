package com.espeditomelo.myblog.service;

import com.espeditomelo.myblog.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    List<Category> findAllByNameAsc();
    Optional<Category> findByName(String name);
}
