package com.espeditomelo.myblog.service.serviceImpl;

import com.espeditomelo.myblog.model.Category;
import com.espeditomelo.myblog.model.repository.CategoryRepository;
import com.espeditomelo.myblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllByNameAsc() {
        return categoryRepository.findAllByNameAsc();
    }
}
