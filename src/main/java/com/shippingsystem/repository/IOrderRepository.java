package com.shippingsystem.repository;

import com.shippingsystem.models.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order,String> {

    @Query("select o from Order o join OrderStatus os on o.id = os.order.id where os.shipper.email=:email" )
    public List<Order> findOrderOfShipper(String email);

    @Query("select o from Order o join OrderStatus os on o.id = os.order.id where os.value !=   4")
    public  List<Order> findOrderNotComplete();

    @Query("select o from Order o join OrderStatus os on o.id = os.order.id where o.receiveAddress = :receiveAddress and os.value != 4 ")
    public List<Order> findByReceiveAddressNotComplete(String receiveAddress);

    @Query("select o from Order o join OrderStatus os on o.id = os.order.id where o.sendAddress = :sendAddress and os.value != 4 ")
    public List<Order> findBySendAddressNotComplete(String sendAddress);

    @Query("select o from Order o join OrderStatus os on o.id = os.order.id where o.sendAddress = :sendAddress and o.receiveAddress = :receiveAddress and os.value != 4 ")
    public List<Order> findBySendAddressAndReceiveAddressNotComplete(String sendAddress, String receiveAddress);

    public List<Order> findByReceiveAddress(String receiveAddress);
}
