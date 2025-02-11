package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    Page<User> findAll(Pageable pageable);
}
