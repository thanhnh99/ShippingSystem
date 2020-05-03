package com.shippingsystem.repository;

import com.shippingsystem.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemRepository extends JpaRepository<Item,Long> {
}
