package com.laptopshop.laptopshop.service;

import org.springframework.stereotype.Service;

import com.laptopshop.laptopshop.domain.Cart;
import com.laptopshop.laptopshop.domain.User;
import com.laptopshop.laptopshop.repository.CartRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart fetchCartByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public Cart fetchCartById(long id) {
        return this.cartRepository.findById(id);
    }

    public Cart saveCart(Cart cart) {
        return this.cartRepository.save(cart);
    }

}
