package com.laptopshop.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.laptopshop.laptopshop.service.OrderService;
import com.laptopshop.laptopshop.service.ProductService;
import com.laptopshop.laptopshop.service.UserService;

@Controller
public class DashBoardController {
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public DashBoardController(UserService userService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        long numOfUser = this.userService.countUser();
        long numOfProduct = this.productService.countProduct();
        long numOfOrder = this.orderService.countOrder();
        model.addAttribute("numOfUser", numOfUser);
        model.addAttribute("numOfProduct", numOfProduct);
        model.addAttribute("numOfOrder", numOfOrder);
        return "admin/dashboard/show";
    }
}
