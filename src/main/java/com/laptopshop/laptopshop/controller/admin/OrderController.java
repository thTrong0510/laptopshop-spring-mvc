package com.laptopshop.laptopshop.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.laptopshop.laptopshop.domain.Order;
import com.laptopshop.laptopshop.domain.OrderDetail;
import com.laptopshop.laptopshop.domain.User;
import com.laptopshop.laptopshop.service.OrderService;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getOrderPage(Model model, @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            } else {
                // page = 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Order> orders = this.orderService.fetchOrdersPagination(pageable);
        List<Order> ors = orders.getContent();

        List<User> users = new ArrayList();
        for (Order or : ors) {
            users.add(or.getUser());
        }
        model.addAttribute("orders", ors);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrderDetailPage(Model model, @PathVariable long id) {
        Order order = this.orderService.fetchOrderById(id);
        List<OrderDetail> orderDetails = order.getOrderDetails();

        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("order", order);
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {
        Order order = this.orderService.fetchOrderById(id);

        model.addAttribute("order", order);
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String postUpdateOrder(@ModelAttribute("order") Order order) {
        Order adjustedOrder = this.orderService.fetchOrderById(order.getId());
        adjustedOrder.setStatus(order.getStatus());
        this.orderService.saveOrder(adjustedOrder);
        return "redirect:/admin/order";
    }

    @PostMapping("/admin/order/delete/{id}")
    public String postDeleteOrder(@PathVariable long id) {
        this.orderService.deleteOrderById(id);
        return "redirect:/admin/order";
    }
}
