package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.domain.entities.Category;
import org.ibtissam.dadesadventures.exception.category.CategoryAlreadyExistException;
import org.ibtissam.dadesadventures.exception.category.CategoryNotFoundException;
import org.ibtissam.dadesadventures.repository.CategoryRepository;
import org.ibtissam.dadesadventures.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }


    @Override
    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
    }
    @Override
    public Category create(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistException("Category with name '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(UUID id, Category categoryRequest) {
        Category existingCategory = findById(id);
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new CategoryAlreadyExistException("Category with name '" + categoryRequest.getName() + "' already exists.");
        }
        existingCategory.setName(categoryRequest.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(UUID id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> searchByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

}
