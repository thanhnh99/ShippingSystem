package com.shippingsystem.repository;

import com.shippingsystem.models.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItemRepository extends JpaRepository<Item,String> {
    public  Item findByCode(String code);
    @Query
    public Optional<Item> findById(String id);

    public Item getOne(String id);
}
