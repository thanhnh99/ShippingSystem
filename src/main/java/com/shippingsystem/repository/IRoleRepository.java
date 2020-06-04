package com.shippingsystem.repository;

import com.shippingsystem.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role,String> {
    Role getFirstByRoleName(String name);
}
