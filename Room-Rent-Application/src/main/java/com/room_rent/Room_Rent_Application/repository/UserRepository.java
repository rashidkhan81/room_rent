package com.room_rent.Room_Rent_Application.repository;

import com.room_rent.Room_Rent_Application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
   Optional<User> findByEmail(String email);
   boolean existsByEmail(String email);
}
