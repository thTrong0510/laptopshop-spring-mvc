package com.laptopshop.laptopshop.service;

import java.util.List;
import java.util.Optional;

import com.laptopshop.laptopshop.domain.User;
import com.laptopshop.laptopshop.domain.DTO.ProductCriteriaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import com.laptopshop.laptopshop.domain.Cart;
import com.laptopshop.laptopshop.domain.CartDetail;
import com.laptopshop.laptopshop.domain.Order;
import com.laptopshop.laptopshop.domain.Product;
import com.laptopshop.laptopshop.repository.ProductRepository;
import com.laptopshop.laptopshop.service.specification.ProductSpecs;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartDetailService cartDetailService;
    private final CartService cartService;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, CartDetailService cartDetailService,
            CartService cartService, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.cartDetailService = cartDetailService;
        this.cartService = cartService;
    }

    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

    public List<Product> fetchProducts() {
        return this.productRepository.findAll();
    }

    public Page<Product> fetchProductsPagination(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    public long countProduct() {
        return this.productRepository.count();
    }

    public Page<Product> fetchProductsWithSpec(Pageable pageable, double min) {
        return this.productRepository.findAll(ProductSpecs.minPrice(min), pageable);
    }

    public Page<Product> fetchProductsWithSpec(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        if (productCriteriaDTO.getTarget() == null
                && productCriteriaDTO.getFactory() == null
                && productCriteriaDTO.getPrice() == null) {
            return this.productRepository.findAll(page);
        }
        Specification<Product> combinedSpec = Specification.where(null);
        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        if (productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()) {
            Specification<Product> currentSpecs = this.buildPriceSpecification(productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        return this.productRepository.findAll(combinedSpec, page);
    }

    public Specification<Product> buildPriceSpecification(List<String> price) {
        Specification<Product> combinedSpec = Specification.where(null); // disconjunction
        for (String p : price) {
            double min = 0;
            double max = 0;
            // Set the appropriate min and max based on the price range string
            switch (p) {
                case "under-10-million":
                    min = 1;
                    max = 10000000;
                    break;
                case "10-15-million":
                    min = 10000000;
                    max = 15000000;
                    break;
                case "15-20-million":
                    min = 15000000;
                    max = 20000000;
                    break;
                case "over-20-million":
                    min = 20000000;
                    max = 200000000;
                    break;
            }
            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec);
            }
        }
        return combinedSpec;
    }

    public void addProductToCart(HttpSession session, long productId, long quantity) {
        User user = this.userService.fetchUserByEmail((String) session.getAttribute("email"));
        if (user != null) {
            Cart cart = this.cartService.fetchCartByUser(user);
            if (cart == null) {
                // create new cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setTotal_quantity(1);

                cart = this.cartService.saveCart(otherCart);
            }

            Product product = fetchProductById(productId).get();
            Boolean ExistedCartDetail = this.cartDetailService.checkExistedCartDetail(cart, product);

            if (ExistedCartDetail) {
                this.cartDetailService.updateCartDetail(cart, product,
                        this.cartDetailService.fetchCartDetailByCartAndProduct(cart, product).getQuantity() + quantity);
            } else {
                CartDetail cartDetail = new CartDetail();
                // save cart detail into cart
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(quantity);

                this.cartDetailService.saveCartDetail(cartDetail);
            }
            cart.setTotal_quantity(this.cartDetailService.countCartDetailsByCart(cart));
            session.setAttribute("numberOfCartDetails", cart.getTotal_quantity());
        }
    }

}
