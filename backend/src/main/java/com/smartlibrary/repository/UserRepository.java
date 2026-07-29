package com.smartlibrary.repository;

import com.smartlibrary.entity.User;
import com.smartlibrary.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    
    long countByRole(Role role);
}
