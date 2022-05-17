package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AppUser;

public interface userRepository extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);
}
