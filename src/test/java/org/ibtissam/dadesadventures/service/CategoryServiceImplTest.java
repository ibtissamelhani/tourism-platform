package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.domain.entities.Category;
import org.ibtissam.dadesadventures.exception.category.CategoryAlreadyExistException;
import org.ibtissam.dadesadventures.exception.category.CategoryNotFoundException;
import org.ibtissam.dadesadventures.repository.CategoryRepository;
import org.ibtissam.dadesadventures.service.implementation.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }


    @Test
    void CategoryService_findAll_returnsPageOfAllCategories() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        Category category1 = Category.builder()
                .id(UUID.randomUUID())
                .name("Category 1")
                .build();
        Category category2 = Category.builder()
                .id(UUID.randomUUID())
                .name("Category 2")
                .build();

        List<Category> expectedList = List.of(category1, category2);
        Page<Category> expectedPage = new PageImpl<>(expectedList, pageable, expectedList.size());
        when(categoryRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<Category> resultedPage = categoryService.findAll(pageable);

        // Then
        verify(categoryRepository).findAll(pageable);
        assertEquals(2, resultedPage.getContent().size());
        assertEquals(expectedList, resultedPage.getContent());
    }

    @Test
    void CategoryService_findById_returnsCategoryWhenFound() {
        // Given
        UUID id = UUID.randomUUID();
        Category category = Category.builder().id(id).name("Category 1").build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // When
        Category resultedCategory = categoryService.findById(id);

        // Then
        verify(categoryRepository).findById(id);
        assertEquals("Category 1", resultedCategory.getName());
    }

    @Test
    void CategoryService_findById_throwsExceptionWhenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.findById(id));
        assertEquals("Category not found.", exception.getMessage());
        verify(categoryRepository).findById(id);
    }

    @Test
    void CategoryService_create_returnsCategoryWhenSuccessful() {
        // Given
        Category category = Category.builder().id(UUID.randomUUID()).name("New Category").build();
        when(categoryRepository.existsByName("New Category")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        // When
        Category resultedCategory = categoryService.create(category);

        // Then
        verify(categoryRepository).existsByName("New Category");
        verify(categoryRepository).save(category);
        assertEquals("New Category", resultedCategory.getName());
    }

    @Test
    void CategoryService_create_throwsExceptionWhenAlreadyExists() {
        // Given
        Category category = Category.builder().id(UUID.randomUUID()).name("Existing Category").build();
        when(categoryRepository.existsByName("Existing Category")).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(CategoryAlreadyExistException.class, () -> categoryService.create(category));
        assertTrue(exception.getMessage().contains("Category with name 'Existing Category' already exists."));
        verify(categoryRepository).existsByName("Existing Category");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void CategoryService_update_returnsUpdatedCategoryWhenSuccessful() {
        // Given
        UUID id = UUID.randomUUID();
        Category existingCategory = Category.builder().id(id).name("Old Name").build();
        Category updateRequest = Category.builder().name("Updated Name").build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Updated Name")).thenReturn(false);
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        // When
        Category resultedCategory = categoryService.update(id, updateRequest);

        // Then
        verify(categoryRepository).findById(id);
        verify(categoryRepository).existsByName("Updated Name");
        verify(categoryRepository).save(existingCategory);
        assertEquals("Updated Name", resultedCategory.getName());
    }

    @Test
    void CategoryService_update_throwsExceptionWhenNameAlreadyExists() {
        // Given
        UUID id = UUID.randomUUID();
        Category existingCategory = Category.builder().id(id).name("Old Name").build();
        Category updateRequest = Category.builder().name("Existing Name").build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Existing Name")).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(CategoryAlreadyExistException.class, () -> categoryService.update(id, updateRequest));
        assertTrue(exception.getMessage().contains("Category with name 'Existing Name' already exists."));
        verify(categoryRepository).findById(id);
        verify(categoryRepository).existsByName("Existing Name");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void CategoryService_delete_deletesCategoryWhenFound() {
        // Given
        UUID id = UUID.randomUUID();
        Category category = Category.builder().id(id).name("Category To Delete").build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // When
        categoryService.delete(id);

        // Then
        verify(categoryRepository).findById(id);
        verify(categoryRepository).delete(category);
    }

    @Test
    void CategoryService_delete_throwsExceptionWhenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.delete(id));
        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void CategoryService_searchByName_returnsMatchingCategories() {
        // Given
        String name = "cat";
        Category category1 = Category.builder().id(UUID.randomUUID()).name("Cat One").build();
        Category category2 = Category.builder().id(UUID.randomUUID()).name("Cat Two").build();
        List<Category> expectedList = List.of(category1, category2);
        when(categoryRepository.findByNameContainingIgnoreCase(name)).thenReturn(expectedList);

        // When
        List<Category> resultedList = categoryService.searchByName(name);

        // Then
        verify(categoryRepository).findByNameContainingIgnoreCase(name);
        assertEquals(2, resultedList.size());
        assertEquals(expectedList, resultedList);
    }

}