package com.laptopshop.laptopshop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long quantity;
    private double price;

    // order_id
    // OrderDetail many to one order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // product_id
    // OrderDetail many to one product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    // chỉ khai báo quan hệ 1 chiều giữa OrderDetail và Product để chỉ xác nhận
    // trong OrderDatail xuất hiện Product nào
    // Ngược lại chúng ta ko cần quan tâm 1 Product xuất hiển trong bao nhiêu
    // OrderDetail

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderDetail [id=" + id + ", quantity=" + quantity + ", price=" + price + ", order=" + order
                + ", product=" + product + "]";
    }
}
