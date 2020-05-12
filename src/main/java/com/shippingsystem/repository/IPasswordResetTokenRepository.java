package com.shippingsystem.repository;

import com.shippingsystem.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    public PasswordResetToken findByToken(String token);
}
