package com.example.application.security;

import com.example.application.data.Customer;
import com.example.application.data.CustomerRepository;
import com.example.application.data.Staff;
import com.example.application.data.StaffRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public UserDetailsServiceImpl(CustomerRepository customerRepository,
                                  StaffRepository staffRepository){
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByEmail(email)
                .orElse(null);

        if(staff != null){
            return new org.springframework.security.core.userdetails.User(
                    staff.getEmail(),
                    staff.getHashedPassword(),
                    getAuthorities(staff)
            );
        }

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user present with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                customer.getEmail(),
                customer.getHashedPassword(),
                getAuthorities(customer)
        );
    }

    private static List<GrantedAuthority> getAuthorities(Staff staff){
        return staff.getRole().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    private static List<GrantedAuthority> getAuthorities(Customer customer){
        return customer.getRole().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
