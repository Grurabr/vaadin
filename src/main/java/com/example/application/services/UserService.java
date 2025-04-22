package com.example.application.services;

import com.example.application.data.CustomerRepository;
import com.example.application.data.StaffRepository;
import com.example.application.data.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public UserService(CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    public Optional<? extends User> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(user -> (User) user)
                .or(() -> staffRepository.findByEmail(email).map(user -> (User) user));
    }
}
