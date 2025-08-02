package com.laptopshop.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.laptopshop.laptopshop.domain.Cart;
import com.laptopshop.laptopshop.domain.CartDetail;
import com.laptopshop.laptopshop.domain.Order;
import com.laptopshop.laptopshop.domain.Product;
import com.laptopshop.laptopshop.domain.User;
import com.laptopshop.laptopshop.domain.DTO.OrderDTO;
import com.laptopshop.laptopshop.domain.DTO.RegisterDTO;
import com.laptopshop.laptopshop.service.CartDetailService;
import com.laptopshop.laptopshop.service.CartService;
import com.laptopshop.laptopshop.service.OrderService;
import com.laptopshop.laptopshop.service.ProductService;
import com.laptopshop.laptopshop.service.UserService;

@Controller
public class HomePageController {
    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CartDetailService cartDetailService;
    private final CartService cartService;
    private final OrderService orderService;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder,
            CartDetailService cartDetailService, CartService cartService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.cartDetailService = cartDetailService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerDTO") @Valid RegisterDTO registerDTO,
            BindingResult userBindingResult) {
        List<FieldError> errors = userBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }
        if (userBindingResult.hasErrors()) {
            return "client/auth/register";
        }
        User user = this.userService.registerDTOtoUser(registerDTO);
        String hashedPw = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPw);
        user.setRole(this.userService.fetchRoleByName("USER").get());
        this.userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "client/auth/login";
    }

    @GetMapping("/access-deny")
    public String getDenyPage() {
        return "client/auth/deny";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        float total_price = 0;
        Cart cart = this.cartService
                .fetchCartByUser(userService.fetchUserByEmail((String) session.getAttribute("email")));
        List<CartDetail> cartDetails = cart == null
                ? new ArrayList<CartDetail>()
                : cart.getCartDetails(); // nó sẽ tự join 2 table cho chúng ta

        for (CartDetail cartDetail : cartDetails) {
            total_price += cartDetail.getPrice();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("total_price", total_price);
        return "client/cart/show";
    }

    @PostMapping("/confirm-checkout")
    public String getCheckoutPage(Model model, @ModelAttribute("cart") Cart cart) {
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.cartDetailService.updateCartDetailsBeforeCheckout(cartDetails);
        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        float total_price = 0;
        Cart cart = this.cartService
                .fetchCartByUser(userService.fetchUserByEmail((String) session.getAttribute("email")));
        List<CartDetail> cartDetails = cart == null
                ? new ArrayList<CartDetail>()
                : cart.getCartDetails(); // nó sẽ tự join 2 table cho chúng ta

        for (CartDetail cartDetail : cartDetails) {
            total_price += cartDetail.getPrice();
        }
        OrderDTO orderDTO = new OrderDTO();

        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("total_price", total_price);
        return "client/checkout/show";
    }

    @GetMapping("/placed-order")
    public String getThankYouPage(
            Model model,
            @RequestParam("vnp_ResponseCode") Optional<String> vnpayResponseCode,
            @RequestParam("vnp_TxnRef") Optional<String> paymentRef) {

        if (vnpayResponseCode.isPresent() && paymentRef.isPresent()) {
            // thanh toán qua VNPAY, cập nhật trạng thái order
            String paymentStatus = vnpayResponseCode.get().equals("00")
                    ? "PAYMENT_SUCCEED"
                    : "PAYMENT_FAILED";
            this.orderService.updatePaymentStatus(paymentRef.get(), paymentStatus);
        }

        return "client/checkout/placed";
    }

    @GetMapping("/order-history")
    public String getHistoryPage(Model model) {
        List<Order> orders = this.orderService.fetchOrders();
        model.addAttribute("orders", orders);
        return "client/history/show";
    }
}
