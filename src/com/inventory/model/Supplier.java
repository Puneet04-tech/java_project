package com.inventory.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Supplier entity class representing product suppliers
 */
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String supplierId;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private double rating; // 0.0 to 5.0
    private int totalOrders;
    private LocalDateTime registeredDate;
    private boolean active;
    
    // Constructor
    public Supplier(String supplierId, String name, String contactPerson, 
                    String phone, String email, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rating = 0.0;
        this.totalOrders = 0;
        this.registeredDate = LocalDateTime.now();
        this.active = true;
    }
    
    // Default constructor
    public Supplier() {
        this.registeredDate = LocalDateTime.now();
        this.active = true;
        this.rating = 0.0;
        this.totalOrders = 0;
    }
    
    // Getters and Setters
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContactPerson() {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        if (rating < 0.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }
        this.rating = rating;
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public LocalDateTime getRegisteredDate() {
        return registeredDate;
    }
    
    public void setRegisteredDate(LocalDateTime registeredDate) {
        this.registeredDate = registeredDate;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    // Business methods
    public void incrementOrderCount() {
        this.totalOrders++;
    }
    
    public void updateRating(double newRating) {
        if (newRating < 0.0 || newRating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }
        // Calculate average rating
        if (this.totalOrders == 0) {
            this.rating = newRating;
        } else {
            this.rating = ((this.rating * this.totalOrders) + newRating) / (this.totalOrders + 1);
        }
    }
    
    public String getPerformanceLevel() {
        if (rating >= 4.5) return "Excellent";
        if (rating >= 3.5) return "Good";
        if (rating >= 2.5) return "Average";
        if (rating >= 1.5) return "Below Average";
        return "Poor";
    }
    
    @Override
    public String toString() {
        return String.format("Supplier[ID=%s, Name=%s, Contact=%s, Phone=%s, Rating=%.2f, Orders=%d]",
                supplierId, name, contactPerson, phone, rating, totalOrders);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Supplier supplier = (Supplier) obj;
        return supplierId != null && supplierId.equals(supplier.supplierId);
    }
    
    @Override
    public int hashCode() {
        return supplierId != null ? supplierId.hashCode() : 0;
    }
}
