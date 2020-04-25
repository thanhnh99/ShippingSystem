package com.shippingsystem.repository;

import com.shippingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepositytory  extends JpaRepository<User,Long> {
}
