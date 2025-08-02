package com.laptopshop.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laptopshop.laptopshop.domain.Cart;
import com.laptopshop.laptopshop.domain.CartDetail;
import com.laptopshop.laptopshop.domain.Product;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    CartDetail findByCart(Cart cart);

    Boolean existsByCartAndProduct(Cart cart, Product product);// count theo id của cart và product

    CartDetail findByCartAndProduct(Cart cart, Product product);

    long countByCart(Cart cart);

    List<CartDetail> findAllByCart(Cart cart);

    void deleteById(long id);

    CartDetail findById(long id);
}
