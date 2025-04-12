package com.example.application.services;

import com.example.application.data.Staff;
import com.example.application.data.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    private final StaffRepository repository;

    public StaffService(StaffRepository repository){
        this.repository=repository;
    }

    public Staff save(Staff entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Staff> get(Long id){
        return repository.findById(id);
    }

    public List<Staff> getAll(){
        return repository.findAll();
    }


}
