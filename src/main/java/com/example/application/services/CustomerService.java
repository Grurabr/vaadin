package com.example.application.services;

import com.example.application.data.Customer;
import com.example.application.data.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository){
        this.repository = repository;
    }

    public Customer save(Customer entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Customer> get(Long id){
        return repository.findById(id);
    }

    public List<Customer> getAll(){
        return repository.findAll();
    }

    public Boolean customerNameAvailable(String name){
        return repository.findByCustomername(name).isEmpty();
    }

    public Optional<Customer> getByEmail(String email){
        return repository.findByCustomerEmail(email);
    }


}
