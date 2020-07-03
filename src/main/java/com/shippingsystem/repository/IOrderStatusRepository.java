package com.shippingsystem.repository;

import com.shippingsystem.models.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderStatusRepository extends JpaRepository<OrderStatus,String> {
    public List<OrderStatus> findByOrderId(String orderId);

}
