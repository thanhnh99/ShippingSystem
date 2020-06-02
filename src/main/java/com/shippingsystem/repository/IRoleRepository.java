package com.shippingsystem.repository;

import com.shippingsystem.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role,String> {
    Role findByName(String name);
}
