package com.example.hellojpa.api;

import com.example.hellojpa.domain.Customer;
import com.example.hellojpa.domain.CustomerMapper;
import com.example.hellojpa.domain.DTO.CustomerApiDTO;
import com.example.hellojpa.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
        ("/api/customer")
public class CustomerApiController {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    public CustomerApiController(CustomerRepository customerRepository, CustomerMapper customerMapper){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }
    @GetMapping
    public List<CustomerApiDTO> findAll() {
        return customerRepository.findAllProjectedBy(CustomerApiDTO.class);
    }
    @GetMapping("/{customerNbr}")
    public CustomerApiDTO findByCustomerNbr(@PathVariable("customerNbr") String customerNbr) {
        return customerRepository.findByCustomerNbr(customerNbr, CustomerApiDTO.class)
                .orElseThrow(() -> new EntityNotFoundException("Not found"));
    }

    @PostMapping
    public CustomerApiDTO insert(@RequestBody @Valid CustomerApiDTO customerDTO) {
        var customerEntity = customerMapper.map(customerDTO);
        customerEntity = customerRepository.save(customerEntity);
        return customerMapper.map(customerEntity);
    }



}

