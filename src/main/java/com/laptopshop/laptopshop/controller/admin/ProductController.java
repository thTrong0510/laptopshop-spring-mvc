package com.laptopshop.laptopshop.controller.admin;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.laptopshop.laptopshop.domain.Product;
import com.laptopshop.laptopshop.domain.ProductDetail;
import com.laptopshop.laptopshop.service.ProductService;
import com.laptopshop.laptopshop.service.UploadService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model,
            @RequestParam("page") Optional<String> pageOptional) {
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
        Page<Product> products = this.productService.fetchProductsPagination(pageable);
        List<Product> prds = products.getContent();
        model.addAttribute("products", prds);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getProductCreatePage(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product/create";
    }

    @PostMapping(value = "/admin/product/create")
    public String postProductCreate(Model model, @ModelAttribute("product") @Valid Product product,
            BindingResult productBindingResult,
            @RequestParam("nameProductFile") MultipartFile productFile) {
        // view error in terminal
        List<FieldError> errors = productBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>" + error.getField() + " - " +
                    error.getDefaultMessage());
        }

        if (productFile.getSize() > (5 * 1024 * 1024)) {
            return "admin/product/create";
        }

        if (productBindingResult.hasErrors()) {
            return "admin/product/create";
        }
        String nameProductFile = this.uploadService.handleSaveUploadFile(productFile,
                "product");
        product.setImage(nameProductFile);

        this.productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    @PostMapping("/admin/product/delete/{id}")
    public String postProductDelete(@PathVariable long id) {
        this.productService.deleteProductById(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getProductUpdatePage(Model model, @PathVariable long id, HttpSession session) {
        Product product = this.productService.fetchProductById(id).get();
        File pathProduct = this.uploadService.getFileImage("product",
                product.getImage());
        if (session.getAttribute("pathProduct") != null) {
            session.removeAttribute("pathProduct");
        }
        session.setAttribute("pathProduct", pathProduct);
        model.addAttribute("product", product);
        model.addAttribute("pathProduct", session.getAttribute("pathProduct"));
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postProductUpdate(Model model, @ModelAttribute("product") @Valid Product product,
            BindingResult productBindingResult,
            @RequestParam("nameProductFile") MultipartFile productFile, HttpSession session) {
        // view error in terminal
        List<FieldError> errors = productBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>" + error.getField() + " - " +
                    error.getDefaultMessage());
        }
        if (productBindingResult.hasErrors()) {
            model.addAttribute("pathProduct", session.getAttribute("pathProduct"));
            return "admin/product/update";
        }
        Product currentProduct = this.productService.fetchProductById(product.getId()).get();

        if (!productFile.isEmpty()) {
            String nameProductFile = this.uploadService.handleSaveUploadFile(productFile,
                    "product");
            currentProduct.setImage(nameProductFile);
        }

        currentProduct.setFactory(product.getFactory());
        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setQuantity(product.getQuantity());
        currentProduct.setSold(product.getSold());
        currentProduct.setTarget(product.getTarget());

        ProductDetail productDetail = new ProductDetail(product.getProductDetail().getDetailDesc(),
                product.getProductDetail().getShortDesc(), product.getId());
        currentProduct.setProductDetail(productDetail);

        this.productService.saveProduct(currentProduct);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String productDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.fetchProductById(id).get();
        File image = this.uploadService.getFileImage("product", product.getImage());
        model.addAttribute("product", product);
        model.addAttribute("image", image);
        return "admin/product/detail";
    }

}
