package com.laptopshop.laptopshop.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import com.laptopshop.laptopshop.domain.Cart;
import com.laptopshop.laptopshop.domain.CartDetail;
import com.laptopshop.laptopshop.domain.Order;
import com.laptopshop.laptopshop.domain.OrderDetail;
import com.laptopshop.laptopshop.domain.Product;
import com.laptopshop.laptopshop.domain.User;
import com.laptopshop.laptopshop.domain.DTO.OrderDTO;
import com.laptopshop.laptopshop.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;

    public OrderService(OrderRepository orderRepository, CartService cartService,
            OrderDetailService orderDetailService, CartDetailService cartDetailService) {
        this.orderDetailService = orderDetailService;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.cartDetailService = cartDetailService;
    }

    public List<Order> fetchOrders() {
        return this.orderRepository.findAll();
    }

    public Order fetchOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    public void deleteOrderById(long id) {
        this.orderRepository.deleteById(id);
    }

    public long countOrder() {
        return this.orderRepository.count();
    }

    public void placeOrder(HttpSession session, User user, OrderDTO orderDTO, String uuid) {

        // create order
        Order order = new Order();
        order.setUser(user);
        order.setRecieverAddress(orderDTO.getRecieverAddress());
        order.setRecieverMobile(orderDTO.getRecieverMobile());
        order.setRecieverName(orderDTO.getRecieverName());
        order.setStatus("PENDING");

        // VNPay
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setPaymentStatus("PAYMENT_UNPAID");
        order.setPaymentRef(orderDTO.getPaymentMethod().equals("COD") ? "UNKNOWN" : uuid);

        order = this.orderRepository.save(order);
        double total_price = 0;

        // create order detail
        Cart cart = this.cartService.fetchCartByUser(user);
        List<CartDetail> cartDetails = cart.getCartDetails();
        if (cartDetails != null) {

            // handle order detail
            for (CartDetail cd : cartDetails) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cd.getProduct());
                orderDetail.setPrice(cd.getPrice());
                orderDetail.setQuantity(cd.getQuantity());
                this.orderDetailService.saveOrderDetail(orderDetail);
                total_price = total_price + cd.getPrice();
            }

            order.setTotalPrice(total_price);
            this.orderRepository.save(order);

            // delete cart detail and update cart
            for (CartDetail cd : cartDetails) {
                this.cartDetailService.RemoveCartDetail(session, cd.getId());
            }

            // update session
            session.setAttribute("numberOfCartDetails", 0);
        }
    }

    public Page<Order> fetchOrdersPagination(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    // vnpay
    public void updatePaymentStatus(String paymentRef, String paymentStatus) {
        Optional<Order> orderOptional = this.orderRepository.findByPaymentRef(paymentRef);
        if (orderOptional.isPresent()) {
            // update
            Order order = orderOptional.get();
            order.setPaymentStatus(paymentStatus);
            this.orderRepository.save(order);
        }
    }

}
