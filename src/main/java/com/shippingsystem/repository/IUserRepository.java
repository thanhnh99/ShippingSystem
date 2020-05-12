package com.shippingsystem.repository;

import com.shippingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    public User findByUserName(String username);

    public User findByEmail(String email);

    public Optional findById(String id);
}
