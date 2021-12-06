package com.example.NeoCafe.service;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.dto.CategoryDto;
import com.example.NeoCafe.entity.Category;
import com.example.NeoCafe.exception.AlreadyExistsException;
import com.example.NeoCafe.exception.ResourceNotFoundException;
import com.example.NeoCafe.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAllByStatus(Status.ACTIVATE);
    }

    @Transactional
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("категория с такой id  не существует! id = ", id)
        );
        category.setStatus(Status.DELETED);
        categoryRepository.save(category);
    }

    public CategoryDto add(CategoryDto categoryDto) throws AlreadyExistsException {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new AlreadyExistsException("Категория с таким именем уже существует!");
        } else {
            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setStatus(Status.ACTIVATE);
            categoryRepository.save(category);
            return categoryDto;
        }
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("категория с такой id  не существует! id = ", id)
        );
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return categoryDto;
    }
}
