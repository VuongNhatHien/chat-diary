package com.hcmus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);
}
