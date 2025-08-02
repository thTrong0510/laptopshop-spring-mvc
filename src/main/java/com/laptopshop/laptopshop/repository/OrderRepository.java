package com.laptopshop.laptopshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laptopshop.laptopshop.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order order);

    List<Order> findAll();

    Page<Order> findAll(Pageable pageable);

    Order findById(long id);

    Optional<Order> findByPaymentRef(String paymentRef);

    void deleteById(long id);

    long count();
}
