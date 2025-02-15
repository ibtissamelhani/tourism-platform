package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.domain.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface CategoryService {
    Category findById(UUID id);
    Category create(Category category);
    Category update(UUID id, Category categoryRequest);
    void delete(UUID id);
    Page<Category> findAll(Pageable pageable);
}
