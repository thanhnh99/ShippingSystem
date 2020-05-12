package com.shippingsystem.repository;

import antlr.collections.List;
import com.shippingsystem.models.Order;
import com.shippingsystem.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IStockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT  o from Order o JOIN o.orderStatuses os join os.stock st where st.id=?1")
    public Order findOrderByStockId(Long stockId);
}
