package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Role;

public interface roleRepository extends JpaRepository<Role, Integer> {
     Role findByName(String name);
}
