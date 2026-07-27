package com.smartlibrary.service;

import com.smartlibrary.dto.category.CategoryRequest;
import com.smartlibrary.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    CategoryResponse getById(Long id);

    List<CategoryResponse> list();
}
