package com.example.application.data;

import jakarta.persistence.ManyToMany;

import java.time.Duration;
import java.util.List;

public class Operation extends AbstractEntity{
    private String name;
    private float price;
    private Duration executionTime;

    @ManyToMany(mappedBy = "services")
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Duration executionTime) {
        this.executionTime = executionTime;
    }
}
