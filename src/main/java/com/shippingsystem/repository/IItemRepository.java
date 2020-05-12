package com.shippingsystem.repository;

import com.shippingsystem.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemRepository extends JpaRepository<Item,Long> {
    public  Item findByCode(String code);
}
