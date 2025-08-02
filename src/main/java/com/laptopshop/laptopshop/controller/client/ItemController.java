package com.laptopshop.laptopshop.controller.client;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.ui.Model;

import com.laptopshop.laptopshop.domain.Product;
import com.laptopshop.laptopshop.domain.Product_;
import com.laptopshop.laptopshop.domain.User;
import com.laptopshop.laptopshop.domain.DTO.OrderDTO;
import com.laptopshop.laptopshop.domain.DTO.ProductCriteriaDTO;
import com.laptopshop.laptopshop.service.CartDetailService;
import com.laptopshop.laptopshop.service.OrderService;
import com.laptopshop.laptopshop.service.ProductService;
import com.laptopshop.laptopshop.service.VNPayService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {
    final private ProductService productService;
    final private CartDetailService cartDetailService;
    final private OrderService orderService;
    final private VNPayService vnPayService;

    public ItemController(ProductService productService, CartDetailService cartDetailService,
            OrderService orderService, VNPayService vnPayService) {
        this.productService = productService;
        this.cartDetailService = cartDetailService;
        this.orderService = orderService;
        this.vnPayService = vnPayService;
    }

    @GetMapping("/product/{id}")
    public String getMethodName(Model model, @PathVariable long id) {
        Product product = this.productService.fetchProductById(id).get();
        model.addAttribute("product", product);
        return "client/homepage/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long productId = id;
        this.productService.addProductToCart(session, productId, 1);
        return "redirect:/";
    }

    @PostMapping("/add-product-from-view-detail/{id}")
    public String postAddProductFromViewDetail(@PathVariable long id, @RequestParam("quantity") long quantity,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long productId = id;
        this.productService.addProductToCart(session, productId, quantity);
        return "redirect:/product/" + id;
    }

    @PostMapping("/cart/del-cart-detail/{id}")
    public String delCartDetailById(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        this.cartDetailService.RemoveCartDetail(session, id);
        return "redirect:/cart";
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpServletRequest request, @ModelAttribute("orderDTO") OrderDTO orderDTO,
            @RequestParam("totalPrice") String totalPrice) throws UnsupportedEncodingException {
        HttpSession session = request.getSession(false);
        User user = new User();
        user.setId((long) session.getAttribute("id"));
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        this.orderService.placeOrder(session, user, orderDTO, uuid);

        if (!orderDTO.getPaymentMethod().equals("COD")) {
            // redirect: VNPay
            String ip = this.vnPayService.getIpAddress(request);
            String vnUrl = this.vnPayService.generateVNPayURL(Double.parseDouble(totalPrice), uuid, ip);

            return "redirect:" + vnUrl;

        }

        return "redirect:/placed-order";
    }

    @GetMapping("/products")
    public String getProductsPage(Model model, ProductCriteriaDTO productCriteriaDTO, HttpServletRequest request) {
        int page = 1;
        try {
            if (productCriteriaDTO.getPage().isPresent()) {
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            } else {
                // page = 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 6);
        if (productCriteriaDTO.getSort() != null && productCriteriaDTO.getSort().isPresent()) {
            String sort = productCriteriaDTO.getSort().get();
            if (sort.equals("ascending-price")) {
                pageable = PageRequest.of(page - 1, 6, Sort.by(Product_.PRICE).ascending());
            } else if (sort.equals("descending-price")) {
                pageable = PageRequest.of(page - 1, 6, Sort.by(Product_.PRICE).descending());
            }
        }
        Page<Product> products = this.productService.fetchProductsWithSpec(pageable, productCriteriaDTO);

        List<Product> prds = products.getContent();

        // qs giúp lưu lại phần url sau page=... khi nhấn sang page 2, 3, 4, ... thì gán
        // lại phần url này cho nó
        String qs = request.getQueryString();
        if (qs != null && !qs.isBlank()) {
            // remove page
            qs = qs.replace("page=" + page, "");
        }
        model.addAttribute("products", prds);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("queryString", qs);
        return "client/product/show";
    }

}
