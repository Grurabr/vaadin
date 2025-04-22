package com.example.application.security;

import com.example.application.data.Customer;
import com.example.application.data.CustomerRepository;
import com.example.application.data.Staff;
import com.example.application.data.StaffRepository;
import com.example.application.services.CustomerService;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticatedUser {
    private final CustomerRepository customerRepository;
    private final AuthenticationContext authenticationContext;
    private final StaffRepository staffRepository;

    public AuthenticatedUser(AuthenticationContext authenticationContext,
                             CustomerRepository customerRepository,
                             StaffRepository staffRepository){
        this.authenticationContext = authenticationContext;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    @Transactional
    public Optional<Object> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .flatMap(userDetails -> {
                    Optional<Staff> staff = staffRepository.findByEmail(userDetails.getUsername());
                    if (staff.isPresent()){
                        return Optional.of(staff.get());
                    }

                    Optional<Customer> customer = customerRepository.findByEmail(userDetails.getUsername());
                    return customer;

                });
    }

    public void logout() {
        authenticationContext.logout();
    }
}
