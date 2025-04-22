package com.example.application.services;

import com.example.application.data.Operation;
import com.example.application.data.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperationService {

    private final OperationRepository repository;

    public OperationService(OperationRepository repository){
        this.repository = repository;
    }

    public Operation save(Operation entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Operation> get(Long id){
        return repository.findById(id);
    }

    public List<Operation> getAll(){
        return repository.findAll();
    }



}
