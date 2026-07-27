package com.smartlibrary.service.impl;

import com.smartlibrary.dto.category.CategoryRequest;
import com.smartlibrary.dto.category.CategoryResponse;
import com.smartlibrary.entity.Category;
import com.smartlibrary.exception.DuplicateResourceException;
import com.smartlibrary.exception.InvalidOperationException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.mapper.CategoryMapper;
import com.smartlibrary.repository.CategoryRepository;
import com.smartlibrary.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category already exists: " + request.getName());
        }
        Category saved = categoryRepository.save(categoryMapper.toEntity(request));
        log.info("Created category id={} name='{}'", saved.getId(), saved.getName());
        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findCategory(id);
        if (!category.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category already exists: " + request.getName());
        }
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = findCategory(id);
        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            throw new InvalidOperationException("Cannot delete a category that still has books");
        }
        categoryRepository.delete(category);
        log.info("Deleted category id={}", id);
    }

    @Override
    public CategoryResponse getById(Long id) {
        return categoryMapper.toResponse(findCategory(id));
    }

    @Override
    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
