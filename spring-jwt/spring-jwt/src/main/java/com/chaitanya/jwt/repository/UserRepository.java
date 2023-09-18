package com.chaitanya.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chaitanya.jwt.entity.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	public Optional<User> findByEmail(String email);

}
