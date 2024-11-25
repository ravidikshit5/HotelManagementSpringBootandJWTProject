package com.codeWithRavi.Hotel.Server.repository;

import com.codeWithRavi.Hotel.Server.entity.User;
import com.codeWithRavi.Hotel.Server.enumms.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findFirstByEmail(String email);
   Optional<User> findByUserRole(UserRole userRole);
}
