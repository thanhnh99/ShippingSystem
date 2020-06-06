package com.shippingsystem.repository;

import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IStockRepository extends JpaRepository<Stock, String> {
    @Query("SELECT  o from Order o JOIN o.orderStatuses os join os.stock st where st.id=?1")
    public Order findOrderByStockId(String stockId);
}
