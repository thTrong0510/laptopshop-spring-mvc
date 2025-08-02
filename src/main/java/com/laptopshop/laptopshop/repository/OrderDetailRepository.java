package com.laptopshop.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laptopshop.laptopshop.domain.Order;
import com.laptopshop.laptopshop.domain.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<Order, Long> {
    OrderDetail save(OrderDetail orderDetail);

    void deleteById(long id);
}
