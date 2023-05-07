package com.springzoom.springzoom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springzoom.springzoom.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
