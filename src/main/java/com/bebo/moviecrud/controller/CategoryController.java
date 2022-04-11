package com.bebo.moviecrud.controller;

import javax.validation.Valid;

import com.bebo.moviecrud.model.entities.Category;
import com.bebo.moviecrud.model.repositories.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/addcat")
    public String showAddCategoryForm(Category category) {
        return "add-category";
    }
    
    @PostMapping("/addcategory")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-category";
        }
        
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/editcategory/{id}")
    public String showUpdateCategoryForm(@PathVariable("id") long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        
        return "update-category";
    }

    @GetMapping("/deletecategory/{id}")
    public String deleteCategory(@PathVariable("id") long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        categoryRepository.delete(category);
        
        return "redirect:/categories";
    }

    @PostMapping("/updatecategory/{id}")
    public String updateCategory(@PathVariable("id") long id, @Valid Category category, BindingResult result, Model model) {

        category.setCategoryId(id);
        categoryRepository.save(category);

        return "redirect:/categories";
    }

    

    @GetMapping("/categories")
    public String showCategoryList(Model model) {
    model.addAttribute("categories", categoryRepository.findAll());
    return "categories";
}
}
