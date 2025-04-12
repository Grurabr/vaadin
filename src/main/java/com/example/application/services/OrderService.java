package com.example.application.services;

import com.example.application.data.Order;
import com.example.application.data.OrderRepository;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository){
        this.repository = repository;
    }

    public Order save(Order entity) {
        return repository.save(entity);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public Optional<Order> get(Long id){
        return repository.findById(id);
    }

    public List<Order> getAll(){
        return repository.findAll();
    }
}
