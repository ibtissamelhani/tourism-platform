package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository  extends JpaRepository<Category, UUID> {
    List<Category> findByName(String name);
    boolean existsByName(String name);
}
