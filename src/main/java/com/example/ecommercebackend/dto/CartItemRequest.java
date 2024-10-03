package com.example.ecommercebackend.dto;



public class CartItemRequest {
    private Long userId;
    private Long productId;
    private int quantity;
    private int quantityToRemove; // quantity to remove
    private double price;
    private String name;




    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {  // Corrected
        return productId;
    }

    public void setProductId(Long productId) {  // Corrected
        this.productId = productId;
    }

    public int getQuantityToRemove() {
        return quantityToRemove;
    }

    public void setQuantityToRemove(int quantityToRemove) {
        this.quantityToRemove = quantityToRemove;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
