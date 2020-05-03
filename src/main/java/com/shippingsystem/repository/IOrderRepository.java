package com.shippingsystem.repository;

import com.shippingsystem.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order,Long> {
    public List<Order> findByReceiveAddress(String receiveAddress);
}
